package ru.practicum.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class Location {
    @NotNull
    private Double lat;
    @NotNull
    private Double lon;
}
