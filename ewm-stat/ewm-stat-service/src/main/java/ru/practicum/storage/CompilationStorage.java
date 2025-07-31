package ru.practicum.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.model.Compilation;
import ru.practicum.repository.CompilationRepository;

import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class CompilationStorage {
    private final CompilationRepository compilationRepository;

    public List<Compilation> findCompilations(Boolean pinned, Integer from, Integer size) {

        List<Compilation> compilations = compilationRepository.findAllCompilationsNative(pinned, from, size);
        return null;
    }

    public Compilation getCompilationById() {
        return null;
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
