package ru.practicum.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;


@Data
public class NewUserRequest {
    @NotNull
    @Email
    private String email;
    @NotBlank
    @Size(min = 2, max = 250, message = "Название должно быть от 1 до 50 символов")
    private String name;
}
