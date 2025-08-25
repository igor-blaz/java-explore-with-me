package ru.practicum;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class StatsClient {


    private final RestTemplate rest;
    private final String baseUrl;

    public StatsClient(RestTemplate rest,
                       @Value("${stats.base-url}") String baseUrl) {
        this.rest = rest;
        this.baseUrl = baseUrl;
    }

    public EndpointHitDto saveHit(EndpointHitDto dto) {
        return rest.postForObject(baseUrl + "/hit", dto, EndpointHitDto.class);
    }

    public List<ViewStatsDto> getStats(LocalDateTime start, LocalDateTime end,
                                       List<String> uris, boolean unique) {
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder
                .fromHttpUrl(baseUrl + "/stats")
                .queryParam("start", start.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .queryParam("end", end.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .queryParam("unique", unique);
        if (uris != null && !uris.isEmpty()) uris.forEach(u -> uriComponentsBuilder.queryParam("uris", u));
        ResponseEntity<List<ViewStatsDto>> resp = rest.exchange(
                uriComponentsBuilder.toUriString(), HttpMethod.GET, null, new ParameterizedTypeReference<>() {
                });
        return resp.getBody();
    }
}

