package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.model.Location;
import ru.practicum.model.Participation;

public interface ParticipationRepository extends JpaRepository<Participation, Long> {
}
