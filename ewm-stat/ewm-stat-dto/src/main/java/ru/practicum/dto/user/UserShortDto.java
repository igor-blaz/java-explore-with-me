package ru.practicum.dto.user;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UserShortDto {
    @NotNull
    private Long id;
    @NotNull
    private String name;
}
