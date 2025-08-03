package ru.practicum.dto.location;

import lombok.Builder;
import lombok.Data;

import jakarta.validation.constraints.NotNull;

@Data
@Builder
public class LocationDto {
    @NotNull
    private Double lat;
    @NotNull
    private Double lon;
}
