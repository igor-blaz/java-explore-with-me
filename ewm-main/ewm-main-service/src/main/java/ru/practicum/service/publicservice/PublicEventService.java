package ru.practicum.service.publicservice;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.controller.publiccontroller.SortType;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.EventShortDto;
import ru.practicum.dto.participation.ParticipationStatus;
import ru.practicum.exceptions.BadRequestException;
import ru.practicum.mapper.EventMapper;
import ru.practicum.mapper.StringIlikeSqlPattern;
import ru.practicum.model.Event;
import ru.practicum.service.StatsConnector;
import ru.practicum.storage.EventStorage;
import ru.practicum.storage.ParticipationStorage;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PublicEventService {
    private final EventStorage eventStorage;
    private final StatsConnector statsConnector;
    private final ParticipationStorage participationStorage;


    public EventFullDto getEventByIdPublic(Long id) {
        Event event = eventStorage.getPublicEventById(id);
        int participationCount = participationStorage.countByEventAndStatus(event.getId(), ParticipationStatus.CONFIRMED);
        event.setConfirmedRequests(participationCount);
        return statsConnector.getViewsForEvent(event, true);
    }

    public List<EventShortDto> getEventsPublic(
            String text,
            List<Long> categories,
            Boolean paid,
            LocalDateTime rangeStart,
            LocalDateTime rangeEnd,
            boolean onlyAvailable,
            SortType sort,
            int from,
            int size,
            HttpServletRequest request
    ) {


        String textPattern = StringIlikeSqlPattern.makeIlikePattern(text);
        boolean categoriesIsEmpty = categories == null || categories.isEmpty();
        List<Event> events;
        String sortType = null;
        LocalDateTime now = LocalDateTime.now();

        if (rangeStart == null && rangeEnd == null) {
            rangeStart = now;
        } else if (rangeStart != null && rangeEnd != null) {
            if (rangeStart.isAfter(rangeEnd)) {
                throw new BadRequestException("начало события позже завершения. Ошибка");
            }
        }


        if (sort != null) {
            sortType = sort.name();
        }

        if (categoriesIsEmpty) {
            events = eventStorage.getEventsPublicWithoutCategories(textPattern,
                    paid, rangeStart, rangeEnd, onlyAvailable, sortType,
                    from, size);
        } else {
            events = eventStorage.getEventsPublicByCategories(textPattern, categories,
                    paid, rangeStart, rangeEnd, onlyAvailable, sortType,
                    from, size);
        }
        statsConnector.makeHit(request);
        return EventMapper.eventShortDtoList(events);
    }
}
