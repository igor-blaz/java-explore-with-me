package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.model.Category;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {


    @Query(value = """
            SELECT * FROM categories
            LIMIT :size OFFSET :from
            """, nativeQuery = true)
    List<Category> findCategoriesNative(
            @Param("from") int from,
            @Param("size") int size);
}
