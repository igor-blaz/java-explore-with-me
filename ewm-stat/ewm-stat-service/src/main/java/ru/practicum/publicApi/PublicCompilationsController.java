package ru.practicum.publicApi;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.compilation.CompilationDto;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/compilations")
@RequiredArgsConstructor
public class PublicCompilationsController {
    @GetMapping
    public List<CompilationDto> getCompilations(
            @RequestParam(required = false) Boolean pined,
            @RequestParam(defaultValue = "0") Integer from,
            @RequestParam(defaultValue = "10") Integer size
    ) {
        log.info("1. Контроллер getCompilations");

        return null;
    }

    @GetMapping("/{compId}")
    public List<CompilationDto> getCompilationsById(@PathVariable Long compId) {

        return null;
    }

}
