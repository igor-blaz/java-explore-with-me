package ru.practicum.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.EndpointHitDto;
import ru.practicum.StatsClient;
import ru.practicum.controller.publiccontroller.SortType;
import ru.practicum.dto.event.*;
import ru.practicum.dto.location.LocationDto;
import ru.practicum.dto.participation.EventRequestStatusUpdateRequest;
import ru.practicum.dto.participation.EventRequestStatusUpdateResult;
import ru.practicum.dto.participation.ParticipationRequestDto;
import ru.practicum.dto.participation.ParticipationStatusForUpdate;
import ru.practicum.exceptions.ConflictException;
import ru.practicum.mapper.EventMapper;
import ru.practicum.mapper.LocationMapper;
import ru.practicum.mapper.ParticipationMapper;
import ru.practicum.mapper.StringIlikeSqlPattern;
import ru.practicum.model.*;
import ru.practicum.storage.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static ru.practicum.dto.participation.ParticipationStatus.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class EventServiceImpl {
    private final EventStorage eventStorage;
    private final CategoryStorage categoryStorage;
    private final StatsClient statsClient;

public EventFullDto getEventByIdPublic(Long id){
    Event event = eventStorage.getEventById(id);
    return EventMapper.toEventDto(event);
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
        EndpointHitDto hitDto = new EndpointHitDto(
                null,
                "ewm-main-service",
                "/events",
                request.getRemoteAddr(),
                LocalDateTime.now()
        );
        statsClient.saveHit(hitDto);
        String textPattern = StringIlikeSqlPattern.makeIlikePattern(text);
        boolean categoriesIsEmpty = categories == null || categories.isEmpty();
        List<Event> events;
        String sortType = null;
        LocalDateTime now = null;
        if (rangeStart == null && rangeEnd == null) {
            now = LocalDateTime.now();
        }
        if(sort != null){
            sortType = sort.name();
        }


        if (categoriesIsEmpty) {
            events = eventStorage.getEventsPublicWithoutCategories(textPattern,
                    paid, rangeStart, rangeEnd, now, onlyAvailable, sortType,
                    from, size);
        } else {
            events = eventStorage.getEventsPublicByCategories(textPattern, categories,
                    paid, rangeStart, rangeEnd, now, onlyAvailable, sortType,
                    from, size);
        }


        return EventMapper.eventShortDtoList(events);
    }





}
