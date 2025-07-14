package ru.practicum.dto.compilation;

import lombok.Data;
import jakarta.validation.constraints.NotNull;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class NewCompilationDto {
    private List<Long> events;
    private Boolean pinned;
    @NotNull
    private String title;
}
