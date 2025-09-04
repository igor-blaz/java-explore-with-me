package ru.practicum.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.dto.event.UpdateEventAdminRequest;
import ru.practicum.exceptions.BadRequestException;
import ru.practicum.model.Event;
import ru.practicum.storage.CategoryStorage;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class UpdateEventMapper {
    private final CategoryStorage categoryStorage;

    public Event updateEvent(Event event, UpdateEventAdminRequest dto) {
        if (dto.getAnnotation() != null) event.setAnnotation(dto.getAnnotation());
        if (dto.getCategory() != null) event.setCategory(categoryStorage.getCategoryById(dto.getCategory()));
        if (dto.getDescription() != null) event.setDescription(dto.getDescription());
        if (dto.getEventDate() != null) event.setEventDate(dto.getEventDate());
        if (dto.getLocation() != null) event.setLocation(LocationMapper.toEntity(dto.getLocation()));
        if (dto.getPaid() != null) event.setPaid(dto.getPaid());
        if (dto.getParticipantLimit() != null) event.setParticipantLimit(dto.getParticipantLimit());
        if (dto.getRequestModeration() != null) event.setRequestModeration(dto.getRequestModeration());
        if (dto.getTitle() != null) event.setTitle(dto.getTitle());
        if (dto.getEventDate() != null) {
            if (dto.getEventDate().isBefore(LocalDateTime.now())) {
                throw new BadRequestException("Дата начала события не может быть в прошлом");
            }
            event.setEventDate(dto.getEventDate());
        }
        return event;
    }
}
