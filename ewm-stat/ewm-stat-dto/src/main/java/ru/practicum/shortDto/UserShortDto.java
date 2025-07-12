package ru.practicum.shortDto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UserShortDto {
    @NotNull
    private Long id;
    @NotNull
    private String name;
}
