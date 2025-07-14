package ru.practicum.dto.category;

import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
public class CategoryDto {
    private Long id;
    @NotNull
    private String name;
}
