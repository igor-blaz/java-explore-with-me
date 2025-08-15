package ru.practicum;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class StatsClient {
    private final RestTemplate rest;
    private final String baseUrl;

    public StatsClient(RestTemplate rest, String baseUrl) {
        this.rest = rest;
        this.baseUrl = baseUrl;
    }

    public void saveHit(EndpointHitDto dto) {
        rest.postForEntity(baseUrl + "/hit", dto, Void.class);
    }

    public List<ViewStatsDto> getStats(LocalDateTime start, LocalDateTime end,
                                       List<String> uris, boolean unique) {
        UriComponentsBuilder b = UriComponentsBuilder
                .fromHttpUrl(baseUrl + "/stats")
                .queryParam("start", start.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .queryParam("end", end.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .queryParam("unique", unique);
        if (uris != null && !uris.isEmpty()) uris.forEach(u -> b.queryParam("uris", u));
        ResponseEntity<List<ViewStatsDto>> resp = rest.exchange(
                b.toUriString(), HttpMethod.GET, null, new ParameterizedTypeReference<>() {
                });
        return resp.getBody();
    }
}

