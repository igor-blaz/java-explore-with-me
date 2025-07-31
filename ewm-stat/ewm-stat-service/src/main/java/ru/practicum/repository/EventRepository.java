package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.model.Event;
import ru.practicum.model.Location;

public interface EventRepository extends JpaRepository<Event, Long> {
}
