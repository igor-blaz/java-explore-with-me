package ru.practicum.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.category.NewCategoryDto;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.mapper.CategoryMapper;
import ru.practicum.model.Category;
import ru.practicum.repository.CategoryRepository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class CategoryStorage {
    private final CategoryRepository repository;

    public Category addNewCategory(NewCategoryDto newCategoryDto) {
        return repository.save(CategoryMapper.toEntity(newCategoryDto));
    }

    public Category getCategory(Long id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException("Категроия не найдена"));
    }

    public void deleteCategory(Category category) {
        repository.delete(category);
    }


    @Transactional
    public Category updateCategory(Category newCategory, Category oldCategory) {
        if (newCategory.getName().equals(oldCategory.getName())) {
            return oldCategory;
        } else {
            oldCategory.setName(newCategory.getName());
        }
        return oldCategory;
    }

}
