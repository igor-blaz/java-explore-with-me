package ru.practicum.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.dto.category.NewCategoryDto;
import ru.practicum.model.Category;

import java.util.Collections;
import java.util.List;

@UtilityClass
public class CategoryMapper {

    public static List<CategoryDto> toCategoryDtoList(List<Category> categories) {

        if (categories == null || categories.isEmpty()) {
            return Collections.emptyList();
        }
        return categories.stream().map(CategoryMapper::toCategoryDto).toList();
    }

    public static Category toEntity(NewCategoryDto dto) {
        if (dto == null) {
            return null;
        }

        return Category.builder()
                .name(dto.getName())
                .build();
    }

    public static NewCategoryDto toDto(Category category) {
        if (category == null) {
            return null;
        }

        return NewCategoryDto.builder()
                .name(category.getName())
                .build();
    }

    public static CategoryDto toCategoryDto(Category category) {
        if (category == null) {
            return null;
        }

        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }

    public static Category toEntity(CategoryDto categoryDto) {
        if (categoryDto == null) {
            return null;
        }

        return Category.builder()
                .id(categoryDto.getId())
                .name(categoryDto.getName())
                .build();
    }
}

