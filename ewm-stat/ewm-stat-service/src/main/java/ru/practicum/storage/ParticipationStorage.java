package ru.practicum.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.model.Participation;
import ru.practicum.repository.ParticipationRepository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ParticipationStorage {
    private final ParticipationRepository participationRepository;

    public Participation getParticipationById(Long id) {
        return participationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("запрос (participationn) с id: " + id + " не найден"));
    }
}
