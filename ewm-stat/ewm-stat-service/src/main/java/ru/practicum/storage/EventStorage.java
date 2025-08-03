package ru.practicum.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.model.Category;
import ru.practicum.model.Event;
import ru.practicum.repository.EventRepository;

import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class EventStorage {
    private final EventRepository eventRepository;

    public boolean isHasCategory(Category category){
        return eventRepository.existsAllByCategory(category);
    }
}
