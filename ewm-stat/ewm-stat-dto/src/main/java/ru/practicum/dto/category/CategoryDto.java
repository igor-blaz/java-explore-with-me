package ru.practicum.dto.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;


@Data
public class CategoryDto {
    private Long id;
    @NotBlank
    @Size(min = 1, max = 50, message = "Имя должно быть от 1 до 50 символов")
    private String name;
}
