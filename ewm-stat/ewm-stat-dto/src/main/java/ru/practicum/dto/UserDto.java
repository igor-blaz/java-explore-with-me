package ru.practicum.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
public class UserDto {
    @NotNull
    @Email
    private String email;
    private Long id;
    @NotNull
    private String name;
}
