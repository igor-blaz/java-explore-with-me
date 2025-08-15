package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.model.Location;

public interface LocationRepository extends JpaRepository<Location, Long>  {
        boolean existsByLatAndLon(Double lat, Double lon);
}
