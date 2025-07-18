package ru.practicum.privateApi;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.participation.ParticipationRequestDto;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/users/{userId}/requests")
@RequiredArgsConstructor
public class PrivateRequestsController {

    @GetMapping
    public List<ParticipationRequestDto> getUserRequestDto(
            @PathVariable Long userId
    ) {
        return null;
    }

    @PostMapping
    public ParticipationRequestDto addUserRequest(
            @PathVariable Long userId,
            @RequestParam(required = true) Long eventId
    ) {
        return null;
    }

    @PatchMapping("/{requestId}/cancel")
    public ParticipationRequestDto cancelParticipationRequest(
            @PathVariable Long userId,
            @PathVariable Long requestId
    ) {
        return null;
    }
}
