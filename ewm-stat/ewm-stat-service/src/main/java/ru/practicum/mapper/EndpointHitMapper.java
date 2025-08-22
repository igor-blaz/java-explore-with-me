package ru.practicum.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.EndpointHitDto;
import ru.practicum.entity.EndpointHit;

@UtilityClass
public class EndpointHitMapper {
    public static EndpointHit toEntity(EndpointHitDto endpointHitDto) {
        return EndpointHit.builder()
                .app(endpointHitDto.getApp())
                .uri(endpointHitDto.getUri())
                .ip(endpointHitDto.getIp())
                .timestamp(endpointHitDto.getTimestamp())
                .build();
    }

    public static EndpointHitDto toDto(EndpointHit e) {
        EndpointHitDto dto = new EndpointHitDto();
        dto.setId(e.getId());
        dto.setApp(e.getApp());
        dto.setUri(e.getUri());
        dto.setIp(e.getIp());
        dto.setTimestamp(e.getTimestamp());
        return dto;
    }

}
