package ru.practicum.dto.category;

import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
public class NewCategoryDto {
    @NotNull
    private String name;
}
