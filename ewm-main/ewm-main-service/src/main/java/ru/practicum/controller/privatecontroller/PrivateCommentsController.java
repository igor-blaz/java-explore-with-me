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
        return commentService.postComment(userId, newCommentDto);
    }

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable Long userId,
                              @PathVariable Long commentId) {
        commentService.deleteCommentById(userId, commentId);
    }

    @PatchMapping("/{commentId}")
    public CommentDto updateComment(@PathVariable Long userId,
                                    @PathVariable Long commentId,
                                    @Valid @RequestBody CommentUpdateRequestDto update) {
        return commentService.updateComment(userId, commentId, update);

    }

    @GetMapping("/{commentId}")
    public CommentDto getCommentByCommentId(@PathVariable Long userId,
                                            @PathVariable Long commentId) {
        return commentService.getCommentById(userId, commentId);
    }

    @GetMapping
    public Set<CommentDto> getCommentsByUserId(@PathVariable Long userId) {
        return commentService.getCommentsByUserId(userId);
    }

    @GetMapping("/by-event/{eventId}")
    public Set<CommentDto> getCommentsByEventId(@PathVariable Long userId,
                                                @PathVariable Long eventId) {
        return commentService.getCommentsByEventId(userId, eventId);
    }

    @GetMapping("/by-time")
    public Set<CommentDto> getCommentsByTime(
            @PathVariable Long userId,
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
            @RequestParam(required = false) LocalDateTime rangeStart,
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
            @RequestParam(required = false) LocalDateTime rangeEnd) {

        return commentService.getCommentsByTime(userId, rangeStart, rangeEnd);
    }

    @GetMapping("/by-text")
    public Set<CommentDto> getCommentsByText(@PathVariable Long userId,
                                             @RequestParam String text) {
        return commentService.getCommentsByText(userId, text);
    }
}
