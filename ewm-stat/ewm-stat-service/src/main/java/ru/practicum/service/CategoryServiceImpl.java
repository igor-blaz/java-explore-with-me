package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.dto.category.NewCategoryDto;
import ru.practicum.exceptions.ConflictException;
import ru.practicum.mapper.CategoryMapper;
import ru.practicum.model.Category;
import ru.practicum.storage.CategoryStorage;
import ru.practicum.storage.EventStorage;

@Service
@Slf4j
@RequiredArgsConstructor
public class CategoryServiceImpl {
    private final CategoryStorage categoryStorage;
    private final EventStorage eventStorage;

    public CategoryDto addCategory(NewCategoryDto categoryDto) {
        Category category = categoryStorage.addNewCategory(categoryDto);
        return CategoryMapper.toCategoryDto(category);
    }

    public void deleteCategory(Long id) {
        Category category = categoryStorage.getCategory(id);
        boolean eventHasCategory = eventStorage.isHasCategory(category);
        if (eventHasCategory) {
            throw new ConflictException("у этой категории есть связанные события");
        }
        categoryStorage.deleteCategory(category);
    }

    public CategoryDto updateCategory(CategoryDto categoryDto, Long id) {
        Category categoryForUpdate = CategoryMapper.toEntity(categoryDto);
        Category oldCategory = categoryStorage.getCategory(id);
        Category afterUpdate = categoryStorage.updateCategory(categoryForUpdate, oldCategory);
        return CategoryMapper.toCategoryDto(afterUpdate);
    }

}
