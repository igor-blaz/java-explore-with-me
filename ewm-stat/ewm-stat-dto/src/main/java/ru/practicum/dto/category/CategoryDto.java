package ru.practicum.dto.category;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class CategoryDto {
    private Long id;
    @NotNull
    private String name;
}
