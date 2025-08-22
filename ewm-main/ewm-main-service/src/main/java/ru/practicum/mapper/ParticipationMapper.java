package ru.practicum.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.dto.participation.ParticipationRequestDto;
import ru.practicum.model.Participation;

import java.util.Collections;
import java.util.List;

@UtilityClass
public class ParticipationMapper {
    public static ParticipationRequestDto toDto(Participation participation) {

        return ParticipationRequestDto.builder()
                .created(participation.getCreated())
                .event(participation.getEvent().getId())
                .id(participation.getId())
                .requester(participation.getRequester().getId())
                .status(participation.getStatus())
                .build();
    }

    public static List<ParticipationRequestDto> toDtoList(List<Participation> participations) {
        if (participations == null || participations.isEmpty()) {
            return Collections.emptyList();
        }
        return participations.stream().map(ParticipationMapper::toDto).toList();
    }


}
