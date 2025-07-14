package ru.practicum.dto.compilation;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CompilationDto {
    //  private
    @NotNull
    private Long id;
    @NotNull
    private Boolean pinned;
    @NotNull
    private String title;
}
