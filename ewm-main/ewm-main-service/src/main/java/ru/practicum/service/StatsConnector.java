package ru.practicum.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.EndpointHitDto;
import ru.practicum.StatsClient;
import ru.practicum.ViewStatsDto;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.mapper.EventMapper;
import ru.practicum.model.Event;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class StatsConnector {
    private final StatsClient statsClient;

    public void makeHit(HttpServletRequest request) {
        log.info("makeHit");
        EndpointHitDto hitDto = new EndpointHitDto(
                null,
                "ewm-main-service",
                request.getRequestURI(),
                request.getRemoteAddr(),
                LocalDateTime.now()
        );
        log.info("TIME {} ", hitDto.getTimestamp());
        statsClient.saveHit(hitDto);
    }

    public List<EventFullDto> getViewsForEvents(List<Event> events, boolean unique) {
        log.info("ENTER getViewsForEvents: events={}", events == null ? "null" : events.size());

        if (events == null || events.isEmpty()) {
            log.info("getViewsForEvents: empty events -> return []");
            return List.of();
        }

        List<String> uris = events.stream()
                .map(e -> "/events/" + e.getId())
                .distinct()
                .toList();

        if (uris.isEmpty()) {
            log.info("getViewsForEvents: empty uris -> return mapped w/o stats");
            return EventMapper.eventFullDtoList(events);
        }

        List<ViewStatsDto> stats = statsClient.getStats(
                LocalDateTime.of(2000, 1, 1, 0, 0),
                LocalDateTime.of(3000, 1, 1, 0, 0),
                uris,
                unique
        );

        List<Event> eventsWithViews = makeViews(events, stats);

        long nonZero = eventsWithViews.stream()
                .filter(e -> e.getViews() > 0).count();
        log.info("Views computed: total={}, >0={}", eventsWithViews.size(), nonZero);

        return EventMapper.eventFullDtoList(eventsWithViews);
    }


    private List<Event> makeViews(List<Event> events, List<ViewStatsDto> stats) {
        Map<Long, Long> eventViews = new HashMap<>();
        for (ViewStatsDto stat : stats) {
            String uri = stat.getUri();
            log.info("URI {} ", uri);
            String[] uriElements = uri.split("/");
            Long eventId = Long.parseLong(uriElements[uriElements.length - 1]);

            eventViews.put(eventId, stat.getHits());

        }
        for (Event event : events) {
            if (eventViews.containsKey(event.getId())) {
                event.setViews(eventViews.get(event.getId()));
            }
        }
        return events;

    }

    public EventFullDto getViewsForEvent(Event event, boolean getUniqueIps) {
        String uri = "/events/" + event.getId();

        List<ViewStatsDto> stats = statsClient.getStats(
                LocalDateTime.of(2000, 1, 1, 0, 0),
                LocalDateTime.of(3000, 1, 1, 0, 0),
                List.of(uri),
                getUniqueIps
        );

        long views = 0L;
        if (!stats.isEmpty()) {
            views = stats.get(0).getHits();
        }

        event.setViews(views);
        log.info("Получение просмотров {} для события {}", views, event.getId());

        return EventMapper.toEventDto(event);
    }

}
