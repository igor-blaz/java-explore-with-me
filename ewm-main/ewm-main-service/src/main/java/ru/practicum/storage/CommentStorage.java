package ru.practicum.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.model.Comment;
import ru.practicum.repository.CommentRepository;

import java.time.LocalDateTime;
import java.util.Set;

@Slf4j
@Repository
@RequiredArgsConstructor
public class CommentStorage {
    private final CommentRepository repository;

    public Comment getCommentByUserIdAndCommentId(Long userId, Long commentId) {
        return repository.findByIdAndUser_Id(commentId, userId)
                .orElseThrow(() -> new NotFoundException("Комментарий " + commentId + "не найден"));
    }

    public Comment getCommentById(Long commentId) {
        return repository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Комментарий " + commentId + "не найден"));

    }

    public Comment postComment(Comment comment) {
        return repository.save(comment);
    }

    public void deleteCommentByUserIdAndCommId(Long userId, Long commentId) {
        getCommentByUserIdAndCommentId(userId, commentId);
        repository.deleteById(commentId);
    }

    public Set<Comment> getCommentsByUserIdAndEventId(Long userId, Long eventId) {
        return repository.findAllByUser_IdAndEvent_Id(userId, eventId);
    }

    public Set<Comment> getCommentsByUserId(Long userId) {
        return repository.findAllByUser_Id(userId);
    }

    public Set<Comment> getAllowedCommentsByUserId(Long userId) {
        return repository.findAllByIsBannedAndUser_Id(false, userId);
    }

    public Set<Comment> getAllowedCommentsByEventId(Long eventId) {
        return repository.findAllByIsBannedAndEvent_Id(false, eventId);
    }

    public Set<Comment> getCommentsByUserIdAndTime(Long userId,
                                                   LocalDateTime start,
                                                   LocalDateTime end) {
        return repository.findCommentsByUserIdAndTime(userId, start, end);
    }

    public Set<Comment> getCommentsByUserIdAndText(Long userId, String text) {
        return repository.findCommentByText(userId, text);
    }

}
