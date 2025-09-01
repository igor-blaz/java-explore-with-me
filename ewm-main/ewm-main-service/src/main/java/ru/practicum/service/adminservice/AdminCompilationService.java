package ru.practicum.service.adminservice;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.compilation.CompilationDto;
import ru.practicum.dto.compilation.NewCompilationDto;
import ru.practicum.dto.compilation.UpdateCompilationRequest;
import ru.practicum.mapper.CompilationMapper;
import ru.practicum.model.Compilation;
import ru.practicum.model.Event;
import ru.practicum.storage.CompilationStorage;
import ru.practicum.storage.EventStorage;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminCompilationService {

    private final CompilationStorage compilationStorage;
    private final EventStorage eventStorage;


    public CompilationDto addCompilation(NewCompilationDto newCompilationDto) {
        Compilation compilation;
        if (newCompilationDto.getEvents() != null && !newCompilationDto.getEvents().isEmpty()) {
            List<Event> events = eventStorage.getEventsByIds(newCompilationDto.getEvents());
            compilation = CompilationMapper.toEntity(newCompilationDto, events);


        } else {
            compilation = CompilationMapper.toEntity(newCompilationDto, Collections.emptyList());
        }

        Compilation savedCompilation = compilationStorage.addCompilation(compilation);
        return CompilationMapper.toDto(savedCompilation);
    }

    @Transactional
    public CompilationDto updateCompilation(UpdateCompilationRequest dto, Long compId) {
        Compilation comp = compilationStorage.getCompilationById(compId);

        if (dto.getPinned() != null && !Objects.equals(comp.getPinned(), dto.getPinned())) {
            comp.setPinned(dto.getPinned());
        }

        if (dto.getTitle() != null) {
            String t = dto.getTitle().trim();
            comp.setTitle(t);
        }

        if (dto.getEvents() != null) {
            List<Event> events = dto.getEvents().isEmpty()
                    ? Collections.emptyList()
                    : eventStorage.getEventsByIds(dto.getEvents());
            comp.setEvents(events);
        }

        Compilation saved = compilationStorage.addCompilation(comp);
        return CompilationMapper.toDto(saved);
    }


    public void deleteCompilation(Long compId) {
        compilationStorage.getCompilationById(compId);
        compilationStorage.deleteCompilation(compId);
    }
}
