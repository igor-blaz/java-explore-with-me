package ru.practicum.controller.admincontroller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.UpdateEventAdminRequest;
import ru.practicum.service.adminservice.AdminEventService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/admin/events")
@RequiredArgsConstructor
public class AdminEventsController {

    private final AdminEventService eventServiceImpl;


    @PatchMapping("/{eventId}")
    public EventFullDto updateEvent(
            @Valid @RequestBody UpdateEventAdminRequest updateEventAdminRequest,
            @PathVariable Long eventId) {
        return eventServiceImpl.adminUpdateEvent(updateEventAdminRequest, eventId);
    }

    @GetMapping
    public List<EventFullDto> getEvents(
            @RequestParam(required = false) List<Long> users,
            @RequestParam(required = false) List<String> states,
            @RequestParam(required = false) List<Long> categories,
            @RequestParam(required = false) String rangeStart,
            @RequestParam(required = false) String rangeEnd,
            @RequestParam(defaultValue = "0") int from,
            @RequestParam(defaultValue = "10") int size
    ) {
        return eventServiceImpl.adminGetEvents(users, states, categories,
                rangeStart, rangeEnd, from, size);
    }

}
