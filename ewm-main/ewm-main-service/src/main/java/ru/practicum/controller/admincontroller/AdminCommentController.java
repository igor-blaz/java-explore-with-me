package ru.practicum.controller.admincontroller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.comment.CommentAdminBanRequest;
import ru.practicum.dto.comment.CommentDto;
import ru.practicum.service.adminservice.AdminCommentService;

@Slf4j
@RestController
@RequestMapping(path = "/admin/comments")
@RequiredArgsConstructor
public class AdminCommentController {
    private final AdminCommentService commentService;

    @PatchMapping("/{commentId}")
    public CommentDto banComment(@PathVariable Long commentId,
                                 @Valid @RequestBody CommentAdminBanRequest request) {
        return commentService.setBan(commentId, request);
    }
}
