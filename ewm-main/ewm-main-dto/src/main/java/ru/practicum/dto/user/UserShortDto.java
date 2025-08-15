package ru.practicum.dto.user;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class UserShortDto {
    @NotNull
    private Long id;
    @NotNull
    private String name;
}
