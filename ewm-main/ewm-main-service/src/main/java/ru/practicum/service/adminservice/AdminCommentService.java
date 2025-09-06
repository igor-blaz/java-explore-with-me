package ru.practicum.service.adminservice;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.comment.CommentAdminBanRequest;
import ru.practicum.dto.comment.CommentDto;
import ru.practicum.mapper.CommentMapper;
import ru.practicum.model.Comment;
import ru.practicum.storage.CommentStorage;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminCommentService {
    private final CommentStorage commentStorage;

    @Transactional
    public CommentDto setBan(Long commentId, CommentAdminBanRequest banRequest) {
        Comment comment = commentStorage.getCommentById(commentId);
        if (comment.isBanned() != banRequest.isBanned()) {
            comment.setBanned(banRequest.isBanned());
            log.info("Comment {} banned={}", commentId, banRequest.isBanned());
        }
        return CommentMapper.toDto(comment);
    }
}
