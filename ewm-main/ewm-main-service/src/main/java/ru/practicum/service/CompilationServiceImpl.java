package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.dto.compilation.CompilationDto;
import ru.practicum.dto.compilation.NewCompilationDto;
import ru.practicum.mapper.CompilationMapper;
import ru.practicum.model.Compilation;
import ru.practicum.model.Event;
import ru.practicum.storage.CompilationStorage;
import ru.practicum.storage.EventStorage;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CompilationServiceImpl {

    private final CompilationStorage compilationStorage;
    private final EventStorage eventStorage;

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

    public CompilationDto addCompilation(NewCompilationDto newCompilationDto) {
        List<Event> events = eventStorage.getEventsByIds(newCompilationDto.getEvents());
        Compilation compilation = CompilationMapper.toEntity(newCompilationDto, events);
        Compilation savedCompilation = compilationStorage.addCompilation(compilation);
        return CompilationMapper.toDto(savedCompilation);
    }

    public CompilationDto updateCompilation(NewCompilationDto newCompilationDto,
                                            Long compId) {
        Compilation compilation = compilationStorage.getCompilationById(compId);

        if (!compilation.getPinned().equals(newCompilationDto.getPinned())) {
            compilation.setPinned(newCompilationDto.getPinned());
        }
        if (!compilation.getTitle().equals(newCompilationDto.getTitle())) {
            compilation.setTitle(newCompilationDto.getTitle());
        }

        if (newCompilationDto.getEvents() != null && !newCompilationDto.getEvents().isEmpty()) {
            List<Event> events = eventStorage.getEventsByIds(newCompilationDto.getEvents());
            compilation.setEvents(events);
        }

        Compilation saved = compilationStorage.addCompilation(compilation);
        return CompilationMapper.toDto(saved);
    }

    public void deleteCompilation(Long compId) {
        compilationStorage.deleteCompilation(compId);
    }
}
