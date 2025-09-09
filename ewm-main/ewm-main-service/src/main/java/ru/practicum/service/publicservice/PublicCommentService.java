package ru.practicum.service.publicservice;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.dto.comment.CommentDto;
import ru.practicum.dto.event.State;
import ru.practicum.mapper.CommentMapper;
import ru.practicum.model.Comment;
import ru.practicum.storage.CommentStorage;
import ru.practicum.storage.EventStorage;
import ru.practicum.storage.UserStorage;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class PublicCommentService {
    private final CommentStorage storage;
    private final UserStorage userStorage;
    private final EventStorage eventStorage;

    public Set<CommentDto> getCommentsFromEvent(Long eventId) {
        eventStorage.getEventById(eventId);
        Set<Comment> comments = storage.getAllowedCommentsByEventId(eventId);
        Set<Comment> commentsChecked = onlyPublishedEventsFilter(comments);
        return CommentMapper.toSetDto(commentsChecked);
    }

    public Set<CommentDto> getCommentsFromUser(Long userId) {
        userStorage.getUserById(userId);
        Set<Comment> comments = storage.getAllowedCommentsByUserId(userId);
        Set<Comment> commentsChecked = onlyPublishedEventsFilter(comments);
        return CommentMapper.toSetDto(commentsChecked);
    }

    private Set<Comment> onlyPublishedEventsFilter(Set<Comment> comments) {
        return comments.stream()
                .filter(comment -> comment.getEvent()
                        .getState()
                        .equals(State.PUBLISHED))
                .collect(Collectors.toSet());
    }
}



