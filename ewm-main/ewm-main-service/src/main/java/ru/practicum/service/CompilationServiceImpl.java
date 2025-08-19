package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.dto.compilation.CompilationDto;
import ru.practicum.mapper.CompilationMapper;
import ru.practicum.model.Compilation;
import ru.practicum.storage.CompilationStorage;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CompilationServiceImpl {

    private final CompilationStorage compilationStorage;

    public List<CompilationDto> getCompilations(
            Boolean pinned, Integer from, Integer size
    ) {
        List<Compilation> compilation = compilationStorage.findCompilations(pinned, from, size);
        return CompilationMapper.toDtoList(compilation);
    }

    public CompilationDto getCompilationById(Long id) {
        Compilation compilation = compilationStorage.getCompilationById(id);
        return CompilationMapper.toDto(compilation);
    }

    public CompilationDto addCompilation(CompilationDto compilationDto) {
        return null;
    }

    public CompilationDto updateCompilation(CompilationDto compilationDto,
                                            Long compId) {
        return null;
    }

    public void deleteCompilation(Long compId) {

    }
}
