package ru.practicum.controller.admincontroller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.compilation.CompilationDto;

@Slf4j
@RestController
@RequestMapping(path = "/admin/compilations")
@RequiredArgsConstructor
public class AdminCompilationsController {
    @PostMapping
    public CompilationDto addCompilationDto(
            @Valid @RequestBody CompilationDto compilationDto
    ) {
        return null;
    }

    @PatchMapping("/{compId}")
    public CompilationDto updateCompilationDto(
            @Valid @RequestBody CompilationDto compilationDto,
            @PathVariable Long compId
    ) {
        return null;
    }

    @DeleteMapping("/{compId}")
    public void deleteCompilationDto(@PathVariable Long compId){

    }
}
