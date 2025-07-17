package ru.practicum.dto.user;

import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
public class UserShortDto {
    @NotNull
    private Long id;
    @NotNull
    private String name;
}
