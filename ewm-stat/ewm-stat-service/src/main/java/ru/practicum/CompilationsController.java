package ru.practicum;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.compilation.CompilationDto;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/compilations")
@RequiredArgsConstructor
public class CompilationsController {
    @GetMapping
    public List<CompilationDto> getCompilations(
            @RequestParam(required = false) Boolean pined,
            @RequestParam(defaultValue = "0") Integer from,
            @RequestParam(defaultValue = "10") Integer size
    ) {
        return null;
    }

    @GetMapping("/{compId}")
    public List<CompilationDto> getCompilationsById(@PathVariable Long compId) {

        return null;
    }

}
