package ru.practicum.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class UserDto {
    @NotBlank
    @Email
    private String email;
    private Long id;
    @NotBlank
    private String name;
}
