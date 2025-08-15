package ru.practicum.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.dto.compilation.CompilationDto;
import ru.practicum.model.Compilation;

import java.util.Collections;
import java.util.List;

@UtilityClass
public class CompilationMapper {


    public static CompilationDto toDto(Compilation compilation) {
        if (compilation == null) {
            return null;
        }

        return CompilationDto.builder()
                .id(compilation.getId())
                .pinned(compilation.getPinned())
                .title(compilation.getTitle())
                .events(Collections.emptyList()) // или null, если нужно
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


