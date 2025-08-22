package ru.practicum.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.model.Compilation;
import ru.practicum.repository.CompilationRepository;

import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class CompilationStorage {
    private final CompilationRepository compilationRepository;

    public List<Compilation> findCompilations(Boolean pinned, Integer from, Integer size) {
        return compilationRepository.findCompilationsNative(pinned, from, size);
    }


    public Compilation getCompilationById(Long id) {
        return compilationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Подборка с id: " + id + " не найдена"));
    }

    public Compilation updateCompilation() {
        return null;
    }

    public void deleteCompilation() {

    }

    public Compilation addCompilation() {
        return null;
    }


}
