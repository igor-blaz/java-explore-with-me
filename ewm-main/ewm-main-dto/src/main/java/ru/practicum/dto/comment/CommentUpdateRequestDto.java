package ru.practicum.dto.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentUpdateRequestDto {
    @NotBlank
    @Size(min = 1, max = 2000, message = "Комментарий должен быть от 1 до 2000 символов")
    private String text;
}
