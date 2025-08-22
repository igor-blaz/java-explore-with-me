package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ViewStatsDto;
import ru.practicum.entity.EndpointHit;

import java.time.LocalDateTime;
import java.util.List;

public interface HitRepository extends JpaRepository<EndpointHit, Long> {


    @Query("""
                SELECT new ru.practicum.ViewStatsDto(e.app, e.uri, COUNT(e.id))
                FROM EndpointHit e
                WHERE e.timestamp BETWEEN :start AND :end
                GROUP BY e.app, e.uri
                ORDER BY COUNT(e.id) DESC
            """)
    List<ViewStatsDto> findStatsAll(LocalDateTime start, LocalDateTime end);

    @Query("""
                SELECT new ru.practicum.ViewStatsDto(e.app, e.uri, COUNT(e.id))
                FROM EndpointHit e
                WHERE e.timestamp BETWEEN :start AND :end
                  AND e.uri IN :uris
                GROUP BY e.app, e.uri
                ORDER BY COUNT(e.id) DESC
            """)
    List<ViewStatsDto> findStatsAllByUris(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query("""
                SELECT new ru.practicum.ViewStatsDto(e.app, e.uri, COUNT(DISTINCT e.ip))
                FROM EndpointHit e
                WHERE e.timestamp BETWEEN :start AND :end
                GROUP BY e.app, e.uri
                ORDER BY COUNT(DISTINCT e.ip) DESC
            """)
    List<ViewStatsDto> findStatsUnique(LocalDateTime start, LocalDateTime end);

    @Query("""
                SELECT new ru.practicum.ViewStatsDto(e.app, e.uri, COUNT(DISTINCT e.ip))
                FROM EndpointHit e
                WHERE e.timestamp BETWEEN :start AND :end
                  AND e.uri IN :uris
                GROUP BY e.app, e.uri
                ORDER BY COUNT(DISTINCT e.ip) DESC
            """)
    List<ViewStatsDto> findStatsUniqueByUris(LocalDateTime start, LocalDateTime end, List<String> uris);


}
