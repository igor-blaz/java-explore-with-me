package ru.practicum.dto.category;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class NewCategoryDto {
    @NotNull
    private String name;
}
