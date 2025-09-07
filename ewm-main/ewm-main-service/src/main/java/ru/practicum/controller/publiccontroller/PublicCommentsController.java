package ru.practicum.controller.publiccontroller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.dto.comment.CommentDto;
import ru.practicum.service.publicservice.PublicCommentService;

import java.util.Set;

@Slf4j
@RestController
@RequestMapping(path = "/comments")
@RequiredArgsConstructor
public class PublicCommentsController {

    private final PublicCommentService service;

    @GetMapping("/by-user/{userId}")
    public Set<CommentDto> getCommentsByUserId(@PathVariable Long userId) {
        return service.getCommentsFromUser(userId);
    }

    @GetMapping("/by-event/{eventId}")
    public Set<CommentDto> getCommentsByEventId(@PathVariable Long eventId) {
        return service.getCommentsFromEvent(eventId);
    }


}
