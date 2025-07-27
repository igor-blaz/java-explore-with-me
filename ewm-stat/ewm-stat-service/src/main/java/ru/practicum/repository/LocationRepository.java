package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.dto.location.LocationDto;

public interface LocationRepository extends JpaRepository<LocationDto, Long>  {

}
