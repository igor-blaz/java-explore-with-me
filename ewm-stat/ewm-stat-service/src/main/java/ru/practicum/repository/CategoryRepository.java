package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.model.Category;
import ru.practicum.model.Location;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
