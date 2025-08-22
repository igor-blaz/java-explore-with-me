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

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CategoryServiceImpl {
    private final CategoryStorage categoryStorage;
    private final EventStorage eventStorage;

    public CategoryDto getCategoryById(Long id) {
        return CategoryMapper.toCategoryDto(categoryStorage.getCategoryById(id));
    }

    public List<CategoryDto> getAllCategories(int from, int size) {
        List<Category> categories = categoryStorage.getAllCategories(from, size);
        return CategoryMapper.toCategoryDtoList(categories);
    }

    public CategoryDto addCategory(NewCategoryDto categoryDto) {
        Category category = categoryStorage.addNewCategory(categoryDto);
        return CategoryMapper.toCategoryDto(category);
    }

    public void deleteCategory(Long id) {
        Category category = categoryStorage.getCategoryById(id);
        boolean eventHasCategory = eventStorage.isHasCategory(category);
        if (eventHasCategory) {
            throw new ConflictException("у этой категории есть связанные события");
        }
        categoryStorage.deleteCategory(category);
    }

    public CategoryDto updateCategory(CategoryDto categoryDto, Long id) {
        Category categoryForUpdate = CategoryMapper.toEntity(categoryDto);
        Category oldCategory = categoryStorage.getCategoryById(id);
        Category afterUpdate = categoryStorage.updateCategory(categoryForUpdate, oldCategory);
        return CategoryMapper.toCategoryDto(afterUpdate);
    }

}
