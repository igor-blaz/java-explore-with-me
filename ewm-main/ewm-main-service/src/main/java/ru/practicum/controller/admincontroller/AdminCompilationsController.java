package ru.practicum.controller.admincontroller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.compilation.CompilationDto;
import ru.practicum.dto.compilation.NewCompilationDto;
import ru.practicum.service.CompilationServiceImpl;

@Slf4j
@RestController
@RequestMapping(path = "/admin/compilations")
@RequiredArgsConstructor
public class AdminCompilationsController {

    private final CompilationServiceImpl compilationService;

    @PostMapping
    public CompilationDto addCompilationDto(
            @Valid @RequestBody NewCompilationDto newCompilationDto
    ) {
        return compilationService.addCompilation(newCompilationDto);
    }

    @PatchMapping("/{compId}")
    public CompilationDto updateCompilationDto(
            @Valid @RequestBody NewCompilationDto newCompilationDto,
            @PathVariable Long compId
    ) {
        return compilationService.updateCompilation(newCompilationDto, compId);
    }

    @DeleteMapping("/{compId}")
    public void deleteCompilationDto(@PathVariable Long compId) {
        compilationService.deleteCompilation(compId);
    }
}
