package ru.practicum.dto.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class NewCategoryDto {
    @NotBlank
    @Size(min = 1, max = 50, message = "Название должно быть от 1 до 50 символов")
    private String name;
}
