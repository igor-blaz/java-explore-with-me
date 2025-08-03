package ru.practicum.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class UserDto {
    @NotNull
    @Email
    private String email;
    private Long id;
    @NotNull
    private String name;
}
