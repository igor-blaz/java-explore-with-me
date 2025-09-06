package ru.practicum.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.dto.comment.CommentDto;
import ru.practicum.dto.comment.NewCommentDto;
import ru.practicum.dto.event.EventShortDto;
import ru.practicum.dto.user.UserShortDto;
import ru.practicum.model.Comment;
import ru.practicum.model.Event;
import ru.practicum.model.User;

import java.util.Set;
import java.util.stream.Collectors;

@UtilityClass
public class CommentMapper {
    public static CommentDto toDto(Comment comment) {
        EventShortDto eventShortDto = EventMapper.toEventShortDto(comment.getEvent());
        UserShortDto userShortDto = UserMapper.toUserShortDto(comment.getUser());

        return CommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .author(userShortDto)
                .event(eventShortDto)
                .publishedOn(comment.getPublishedOn())
                .isBanned(comment.isBanned())
                .build();
    }

    public static Set<CommentDto> toSetDto(Set<Comment> commentSet) {
        return commentSet.stream().map(CommentMapper::toDto).collect(Collectors.toSet());
    }

    public static Comment toModelFromNewDto(NewCommentDto commentDto, User user, Event event) {
        return Comment.builder()
                .id(0L)
                .event(event)
                .text(commentDto.getText())
                .isBanned(false)
                .publishedOn(commentDto.getPublishedOn())
                .user(user)
                .build();
    }

    public static Comment toModel(CommentDto commentDto, User user, Event event) {
        return Comment.builder()
                .id(commentDto.getId())
                .event(event)
                .text(commentDto.getText())
                .isBanned(commentDto.isBanned())
                .publishedOn(commentDto.getPublishedOn())
                .user(user)
                .build();
    }
}
