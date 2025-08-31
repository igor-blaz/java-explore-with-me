package ru.practicum.service.adminservice;

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
public class AdminCategoryService {
    private final CategoryStorage categoryStorage;
    private final EventStorage eventStorage;

    public CategoryDto addCategory(NewCategoryDto categoryDto) {
        existByNameCheck(categoryDto.getName());
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
    private void existByNameCheck(String name){
        if(categoryStorage.isExistsByName(name)){
            throw new ConflictException("Имя категории уже занято");
        }
    }

    public CategoryDto updateCategory(CategoryDto categoryDto, Long id) {
        existByNameCheck(categoryDto.getName());
        Category categoryForUpdate = CategoryMapper.toEntity(categoryDto);
        Category oldCategory = categoryStorage.getCategoryById(id);
        Category afterUpdate = categoryStorage.updateCategory(categoryForUpdate, oldCategory);
        return CategoryMapper.toCategoryDto(afterUpdate);
    }
}
