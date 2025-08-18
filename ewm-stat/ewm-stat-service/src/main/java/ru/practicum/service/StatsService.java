package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.EndpointHitDto;
import ru.practicum.ViewStatsDto;
import ru.practicum.entity.EndpointHit;
import ru.practicum.mapper.EndpointHitMapper;
import ru.practicum.storage.StatsStorage;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class StatsService {

    private final StatsStorage statsStorage;

    public List<ViewStatsDto> getViews(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        return statsStorage.getHits(start, end, uris, unique);
    }

    public EndpointHitDto postHit(EndpointHitDto endpointHitDto) {
        EndpointHit endpointHit = statsStorage.postHit(endpointHitDto);
        return EndpointHitMapper.toDto(endpointHit);
    }
}
