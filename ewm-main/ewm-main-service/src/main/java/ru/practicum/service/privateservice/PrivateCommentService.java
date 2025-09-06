package ru.practicum.service.privateservice;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.comment.CommentDto;
import ru.practicum.dto.comment.CommentUpdateRequestDto;
import ru.practicum.dto.comment.NewCommentDto;
import ru.practicum.exceptions.BadRequestException;
import ru.practicum.exceptions.ConflictException;
import ru.practicum.mapper.CommentMapper;
import ru.practicum.mapper.StringIlikeSqlPattern;
import ru.practicum.mapper.update.UpdateCommentMapper;
import ru.practicum.model.Comment;
import ru.practicum.model.Event;
import ru.practicum.model.User;
import ru.practicum.storage.CommentStorage;
import ru.practicum.storage.EventStorage;
import ru.practicum.storage.UserStorage;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class PrivateCommentService {
    private final CommentStorage storage;
    private final UserStorage userStorage;
    private final EventStorage eventStorage;

    public CommentDto postComment(Long userId, NewCommentDto newCommentDto) {
        if (!Objects.equals(userId, newCommentDto.getUserId())) {
            throw new BadRequestException("Вы не можете публиковать комментарий, который вам не принадлежит");
        }
        User author = userStorage.getUserById(userId);
        Event event = eventStorage.getEventByUserId(newCommentDto.getEventId(), userId);

        Comment comment = CommentMapper.toModelFromNewDto(newCommentDto, author, event);
        Comment postedComment = storage.postComment(comment);
        return CommentMapper.toDto(postedComment);
    }

    @Transactional
    public CommentDto updateComment(Long userId, Long commentId, CommentUpdateRequestDto update) {
        Comment oldComment = storage.getCommentByUserIdAndCommentId(userId, commentId);
        if (oldComment.isBanned()) {
            throw new ConflictException("Ваш комментарий забанен администратором," +
                    " вы не можете его менять");
        }
        Comment updatedComment = UpdateCommentMapper.updateComment(update, oldComment);
        return CommentMapper.toDto(updatedComment);
    }

    public CommentDto getCommentById(Long userId, Long commentId) {
        Comment comment = storage.getCommentByUserIdAndCommentId(userId, commentId);
        return CommentMapper.toDto(comment);
    }

    public void deleteCommentById(Long userId, Long commentId) {
        storage.deleteCommentById(userId, commentId);
    }

    public Set<CommentDto> getCommentsByEventId(Long userId, Long eventId) {
        Set<Comment> comments = storage.getCommentsByEventId(userId, eventId);
        return CommentMapper.toSetDto(comments);
    }

    public Set<CommentDto> getCommentsByUserId(Long userId) {
        Set<Comment> comments = storage.getCommentsByUserId(userId);
        return CommentMapper.toSetDto(comments);
    }

    public Set<CommentDto> getCommentsByTime(Long userId,
                                             LocalDateTime start,
                                             LocalDateTime end) {
        Set<Comment> comments = storage.getCommentsByTime(userId, start, end);
        return CommentMapper.toSetDto(comments);
    }

    public Set<CommentDto> getCommentsByText(Long userId, String text) {
        String updatedText = StringIlikeSqlPattern.makeIlikePattern(text);
        Set<Comment> comments = storage.getCommentsByText(userId, updatedText);
        return CommentMapper.toSetDto(comments);
    }

}
