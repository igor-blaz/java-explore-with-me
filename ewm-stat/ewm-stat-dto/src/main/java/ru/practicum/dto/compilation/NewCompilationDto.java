package ru.practicum.dto.compilation;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class NewCompilationDto {
    private List<Long> events;
    private Boolean pinned;
    @NotNull
    private String title;
}
