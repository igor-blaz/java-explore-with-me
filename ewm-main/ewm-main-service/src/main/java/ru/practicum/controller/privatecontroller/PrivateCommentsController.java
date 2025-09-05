package ru.practicum.controller.privatecontroller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.comment.CommentDto;
import ru.practicum.dto.comment.CommentUpdateRequestDto;
import ru.practicum.dto.comment.NewCommentDto;
import ru.practicum.service.privateservice.PrivateCommentService;

import java.time.LocalDateTime;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping(path = "/users/{userId}/comments")
@RequiredArgsConstructor
public class PrivateCommentsController {
    private final PrivateCommentService commentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto postComment(@PathVariable Long userId,
                                  @Valid @RequestBody NewCommentDto newCommentDto) {
        return null;
    }

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable Long userId,
                              @PathVariable Long commentId) {

    }

    @PatchMapping
    public CommentDto updateComment(@PathVariable Long userId,
                                    @Valid @RequestBody CommentUpdateRequestDto update) {
        return null;

    }

    @GetMapping("/{commentId}")
    public Set<CommentDto> getCommentByCommentId(@PathVariable Long userId,
                                                 @PathVariable Long commentId) {
        return null;
    }

    @GetMapping
    public Set<CommentDto> getCommentsByUserId(@PathVariable Long userId) {
        return null;
    }

    @GetMapping("/{eventId}")
    public Set<CommentDto> getCommentsByEventId(@PathVariable Long userId,
                                                @PathVariable Long eventId) {
        return null;
    }

    @GetMapping
    public Set<CommentDto> getCommentsByDate(
            @PathVariable Long userId,
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
            @RequestParam(required = false) LocalDateTime rangeStart,
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
            @RequestParam(required = false) LocalDateTime rangeEnd) {

        return null;
    }

    @GetMapping
    public Set<CommentDto> getCommentsByText(@PathVariable Long userId,
                                             @RequestParam String text) {
        return null;
    }
}
