package ru.practicum.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.model.Location;
import ru.practicum.repository.LocationRepository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class LocationStorage {
    private final LocationRepository locationRepository;

    public Location getLocationById(Long id) {
        return locationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Локация с id: " + id + " не найдена"));
    }
    public Location saveLocation(Location location){
        return locationRepository.save(location);
    }


}
