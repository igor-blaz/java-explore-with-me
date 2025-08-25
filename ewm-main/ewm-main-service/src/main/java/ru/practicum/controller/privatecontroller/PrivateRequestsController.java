package ru.practicum.controller.privatecontroller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.participation.ParticipationRequestDto;
import ru.practicum.service.privateservice.PrivateParticipationService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/users/{userId}/requests")
@RequiredArgsConstructor
public class PrivateRequestsController {
    private final PrivateParticipationService participationService;

    @GetMapping
    public List<ParticipationRequestDto> getUserRequestDto(
            @PathVariable Long userId
    ) {
        return participationService.getUserRequestDto(userId);
    }

    @PostMapping
    public ParticipationRequestDto addUserRequest(
            @PathVariable Long userId,
            @RequestParam Long eventId
    ) {
        return participationService.addUserRequest(userId, eventId);
    }

    @PatchMapping("/{requestId}/cancel")
    public ParticipationRequestDto cancelParticipationRequest(
            @PathVariable Long userId,
            @PathVariable Long requestId
    ) {
        return participationService.cancelParticipationRequest(userId, requestId);
    }
}
