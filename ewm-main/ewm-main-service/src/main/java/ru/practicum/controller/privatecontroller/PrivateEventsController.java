package ru.practicum.controller.privatecontroller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.event.*;
import ru.practicum.dto.participation.EventRequestStatusUpdateRequest;
import ru.practicum.dto.participation.EventRequestStatusUpdateResult;
import ru.practicum.dto.participation.ParticipationRequestDto;
import ru.practicum.service.EventServiceImpl;
import ru.practicum.service.privateservice.PrivateEventService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/users/{userId}/events")
@RequiredArgsConstructor
public class PrivateEventsController {

    private final PrivateEventService eventServiceImpl;

    @GetMapping
    public List<EventShortDto> getUserEvents(
            @PathVariable Long userId,
            @RequestParam(required = false) Integer from,
            @RequestParam(required = false) Integer size
    ) {
        return eventServiceImpl.getUserEvents(userId, from, size);
    }

    @PostMapping
    public EventFullDto addNewEvent(
            @PathVariable Long userId,
            @RequestBody NewEventDto newEventDto
    ) {
        return eventServiceImpl.addNewEventDto(userId, newEventDto);
    }

    @GetMapping("/{eventId}")
    public EventFullDto getUserEventsByEventId(
            @PathVariable Long userId,
            @PathVariable Long eventId
    ) {
        return eventServiceImpl.getUserEventsByEventId(eventId, userId);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateEvent(
            @RequestBody UpdateEventUserRequest updateEventUserRequest,
            @PathVariable Long userId,
            @PathVariable Long eventId
    ) {
        return eventServiceImpl.updateEventUserRequest(updateEventUserRequest, eventId, userId);

    }

    @GetMapping("/{eventId}/requests")
    public List<ParticipationRequestDto> getUserParticipationRequests(
            @PathVariable Long userId,
            @PathVariable Long eventId
    ) {
        return eventServiceImpl.getUserRequests(userId, eventId);
    }

    @PatchMapping("/{eventId}/requests")
    public EventRequestStatusUpdateResult updateEventCompilationRequest(
            @RequestBody EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest,
            @PathVariable Long userId,
            @PathVariable Long eventId
    ) {
        return eventServiceImpl.updateRequestStatus(eventRequestStatusUpdateRequest, userId, eventId);
    }


}
