package ru.practicum.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.dto.compilation.CompilationDto;
import ru.practicum.dto.compilation.NewCompilationDto;
import ru.practicum.model.Compilation;
import ru.practicum.model.Event;

import java.util.Collections;
import java.util.List;

@UtilityClass
public class CompilationMapper {

    public static Compilation toEntity(NewCompilationDto newCompilationDto, List<Event> events) {
        if (newCompilationDto == null) {
            return null;
        }
        return Compilation.builder()
                .events(events == null ? List.of() : events)
                .title(newCompilationDto.getTitle())
                .pinned(newCompilationDto.getPinned())
                .build();
    }


    public static CompilationDto toDto(Compilation compilation) {
        if (compilation == null) {
            return null;
        }

        return CompilationDto.builder()
                .id(compilation.getId())
                .pinned(compilation.isPinned())
                .title(compilation.getTitle())
                .events(EventMapper.eventShortDtoList(compilation.getEvents()))
                .build();
    }

    public static List<CompilationDto> toDtoList(List<Compilation> compilations) {
        if (compilations == null) {
            return Collections.emptyList();
        }

        return compilations.stream()
                .map(CompilationMapper::toDto)
                .toList();
    }
}


