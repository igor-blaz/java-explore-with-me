package ru.practicum.dto;

import lombok.Data;

import jakarta.validation.constraints.NotNull;

@Data
public class Location {
    @NotNull
    private Double lat;
    @NotNull
    private Double lon;
}
