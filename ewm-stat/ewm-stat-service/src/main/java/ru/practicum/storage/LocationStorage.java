package ru.practicum.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;
import ru.practicum.repository.LocationRepository;

@Slf4j
@Repository
@RequiredArgsConstructor
@EnableJpaRepositories(basePackages = "ru/practicum/dto/Location.java")
public class LocationStorage {
    private final LocationRepository locationRepository;

}
