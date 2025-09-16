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
import ru.practicum.storage.UserStorage;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminCommentService {
    private final CommentStorage commentStorage;
    private final UserStorage userStorage;

    @Transactional
    public CommentDto setBanByCommentId(Long commentId, CommentAdminBanRequest banRequest) {
        Comment comment = commentStorage.getCommentById(commentId);
        if (comment.isBanned() != banRequest.isBanned()) {
            comment.setBanned(banRequest.isBanned());
            log.info("Comment {} banned={}", commentId, banRequest.isBanned());
        }
        return CommentMapper.toDto(comment);
    }

    @Transactional
    public Set<CommentDto> setBanToUserComments(Long userId, CommentAdminBanRequest banRequest) {
        userStorage.getUserById(userId);
        Set<Comment> comments = commentStorage.getCommentsByUserId(userId);
        Set<Comment> updated = comments.stream()
                .peek(comment -> comment.setBanned(banRequest.isBanned()))
                .collect(Collectors.toSet());

        return CommentMapper.toSetDto(updated);
    }
}
