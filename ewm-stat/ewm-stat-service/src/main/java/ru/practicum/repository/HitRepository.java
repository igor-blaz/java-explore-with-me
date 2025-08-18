package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ViewStatsDto;
import ru.practicum.entity.EndpointHit;

import java.time.LocalDateTime;
import java.util.List;

public interface HitRepository extends JpaRepository<EndpointHit, Long> {


    @Query("""
  select new ru.practicum.ViewStatsDto(e.app, e.uri, count(e))
  from EndpointHit e
  where e.timestamp between :start and :end
    and (:allUris = true or e.uri in :uris)
  group by e.app, e.uri
  order by count(e) desc
""")
    List<ViewStatsDto> findStatsAll(LocalDateTime start, LocalDateTime end,
                                    List<String> uris, boolean allUris);

    @Query("""
  select new ru.practicum.ViewStatsDto(e.app, e.uri, count(distinct e.ip))
  from EndpointHit e
  where e.timestamp between :start and :end
    and (:allUris = true or e.uri in :uris)
  group by e.app, e.uri
  order by count(distinct e.ip) desc
""")
    List<ViewStatsDto> findStatsUnique(LocalDateTime start, LocalDateTime end,
                                       List<String> uris, boolean allUris);




}
