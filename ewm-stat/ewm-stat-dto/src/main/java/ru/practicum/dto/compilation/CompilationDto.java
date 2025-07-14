package ru.practicum.dto.compilation;

import lombok.Data;

import javax.validation.constraints.NotNull;
import jakarta.validation.constraints.NotNull;
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
