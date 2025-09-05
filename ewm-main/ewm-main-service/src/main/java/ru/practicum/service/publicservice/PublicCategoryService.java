package ru.practicum.service.publicservice;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.mapper.CategoryMapper;
import ru.practicum.model.Category;
import ru.practicum.storage.CategoryStorage;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PublicCategoryService {
    private final CategoryStorage categoryStorage;

    public CategoryDto getCategoryById(Long id) {
        return CategoryMapper.toCategoryDto(categoryStorage.getCategoryById(id));
    }

    public List<CategoryDto> getAllCategories(int from, int size) {
        List<Category> categories = categoryStorage.getAllCategories(from, size);
        return CategoryMapper.toCategoryDtoList(categories);
    }
}
