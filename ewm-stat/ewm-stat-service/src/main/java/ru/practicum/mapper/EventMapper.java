package ru.practicum.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.EventShortDto;
import ru.practicum.dto.event.NewEventDto;
import ru.practicum.dto.event.StateAction;
import ru.practicum.model.Category;
import ru.practicum.model.Event;
import ru.practicum.model.Location;
import ru.practicum.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.mapper.CategoryMapper.toCategoryDto;
import static ru.practicum.mapper.LocationMapper.toLocationDto;
import static ru.practicum.mapper.UserMapper.toUserShortDto;

@UtilityClass
public class EventMapper {
    public static EventFullDto toEventDto(Event event) {
        return EventFullDto.builder()
                .annotation(event.getAnnotation())
                .category(toCategoryDto(event.getCategory()))
                .confirmedRequests(event.getConfirmedRequests())
                .createdOn(event.getCreatedOn())
                .description(event.getDescription())
                .eventDate(event.getEventDate())
                .id(event.getId())
                .initiator(toUserShortDto(event.getInitiator()))
                .location(toLocationDto(event.getLocation()))
                .paid(event.getPaid())
                .participantLimit(event.getParticipantLimit())
                .publishedOn(event.getPublishedOn())
                .requestModeration(event.getRequestModeration())
                .state(event.getState())
                .title(event.getTitle())
                .views(event.getViews())
                .build();
    }

    public static Event toEntity(
            NewEventDto newEventDto,
            User user,
            Category category,
            Location location
    ) {
        return Event.builder()
                .annotation(newEventDto.getAnnotation())
                .category(category)
                .eventDate(newEventDto.getEventDate())
                .paid(newEventDto.getPaid())
                .createdOn(LocalDateTime.now())
                .initiator(user)
                .location(location)
                .description(newEventDto.getDescription())
                .title(newEventDto.getTitle())
                .views(0L)
                .participantLimit(newEventDto.getParticipantLimit())
                .requestModeration(newEventDto.getRequestModeration())
                .state(StateAction.PUBLISHED)
                .build();
    }
    public EventShortDto toEventShortDto (Event event){
        return EventShortDto.builder()
                .annotation(event.getAnnotation())
                .category(CategoryMapper.toCategoryDto(event.getCategory()))
                .confirmedRequests(event.getConfirmedRequests())
                .eventDate(event.getEventDate())
                .id(event.getId())
                .initiator(UserMapper.toUserShortDto(event.getInitiator()))
                .paid(event.getPaid())
                .title(event.getTitle())
                .views(event.getViews())
                .build();
    }
    public List<EventShortDto> eventShortDtoList(List<Event> events){
        return events.stream().map(EventMapper::toEventShortDto).toList();
    }

    public List<EventFullDto> eventFullDtoList(List<Event> events) {
        return events.stream().map(EventMapper::toEventDto).toList();
    }


}

