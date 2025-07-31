package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.dto.location.LocationDto;
import ru.practicum.model.Location;

public interface LocationRepository extends JpaRepository<Location, Long>  {

}
