package ru.practicum.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.EndpointHitDto;
import ru.practicum.ViewStatsDto;
import ru.practicum.entity.EndpointHit;
import ru.practicum.mapper.EndpointHitMapper;
import ru.practicum.repository.HitRepository;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class StatsStorage {
    private final HitRepository hitRepository;


    public EndpointHit postHit(EndpointHitDto endpointHitDto) {
        return hitRepository.save(EndpointHitMapper.toEntity(endpointHitDto));
    }

    public List<ViewStatsDto> getHits(LocalDateTime start,
                                      LocalDateTime end,
                                      List<String> uris,
                                      boolean unique) {

        boolean allUris = uris != null && !uris.isEmpty();

        return unique
                ? hitRepository.findStatsUnique(start, end, uris, allUris)
                : hitRepository.findStatsAll(start, end, uris, allUris);
    }
}



