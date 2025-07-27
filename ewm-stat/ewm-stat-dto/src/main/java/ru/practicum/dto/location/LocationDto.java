package ru.practicum.dto.location;

import lombok.Data;

import jakarta.validation.constraints.NotNull;

@Data
public class LocationDto {
    @NotNull
    private Double lat;
    @NotNull
    private Double lon;
}
