package ru.practicum;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class StatsClient {


    private final RestTemplate rest;
    private final String baseUrl;

    public StatsClient(RestTemplate rest,
                       @Value("${stats.url}") String baseUrl) {
        this.rest = rest;
        this.baseUrl = baseUrl;
    }

    public EndpointHitDto saveHit(EndpointHitDto dto) {
        return rest.postForObject(baseUrl + "/hit", dto, EndpointHitDto.class);
    }

    public List<ViewStatsDto> getStats(LocalDateTime start, LocalDateTime end,
                                       List<String> uris, boolean unique) {
        DateTimeFormatter F = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        UriComponentsBuilder b = UriComponentsBuilder
                .fromHttpUrl(baseUrl + "/stats")
                .queryParam("start", start.format(F))
                .queryParam("end", end.format(F))
                .queryParam("unique", unique);

        if (uris != null && !uris.isEmpty()) {
            uris.forEach(u -> b.queryParam("uris", u));
        }

        URI uri = b.encode(StandardCharsets.UTF_8).build().toUri();
        ResponseEntity<List<ViewStatsDto>> resp = rest.exchange(
                uri, HttpMethod.GET, null, new ParameterizedTypeReference<>() {
                }
        );
        return resp.getBody();
    }

}

