package ru.practicum.mapper.update;

import lombok.experimental.UtilityClass;
import ru.practicum.dto.comment.CommentUpdateRequestDto;
import ru.practicum.model.Comment;

@UtilityClass
public class UpdateCommentMapper {

    public Comment updateComment(CommentUpdateRequestDto update, Comment old) {
        if (update.getText() != null) old.setText(update.getText());
        return old;
    }
}
