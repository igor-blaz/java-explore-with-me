package ru.practicum.controller.privatecontroller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.compilation.NewCompilationDto;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.EventShortDto;
import ru.practicum.dto.event.NewEventDto;
import ru.practicum.dto.event.UpdateEventUserRequest;
import ru.practicum.dto.participation.ParticipationRequestDto;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/users/{userId}/events")
@RequiredArgsConstructor
public class PrivateEventsController {

    @GetMapping
    public List<EventShortDto> getUserEvents(
            @PathVariable Long userId,
            @RequestParam(required = false) int from,
            @RequestParam(required = false) int size
    ) {
        return null;
    }

    @PostMapping
    public NewEventDto addNewEvent(
            @PathVariable Long userId
    ) {
        return null;
    }

    @GetMapping("/{eventId}")
    public EventFullDto getUserEventsByEventId(
            @PathVariable Long userId,
            @PathVariable Long eventId
    ) {
        return null;
    }

    @PatchMapping("/{eventId}")
    public UpdateEventUserRequest updateEvent(
            @PathVariable Long userId,
            @PathVariable Long eventId
    ) {
        return null;
    }

    @GetMapping("/{eventId}/requests")
    public List<ParticipationRequestDto> getUserParticipationRequests(
            @PathVariable Long userId,
            @PathVariable Long eventId
    ) {
        return null;
    }

    @PatchMapping("/{eventId}")
    public NewCompilationDto updateEventCompilationRequest(
            @PathVariable Long userId,
            @PathVariable Long eventId
    ) {
        return null;
    }


}
