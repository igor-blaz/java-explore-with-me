package ru.practicum.adminApi.adminCategories;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.category.CategoryDto;

@Slf4j
@RestController
@RequestMapping(path = "/admin/categories")
@RequiredArgsConstructor
public class AdminCategoriesController {

    @PostMapping
    public CategoryDto addCategory(@Valid @RequestBody CategoryDto categoryDto) {
        return new CategoryDto();
    }

    @DeleteMapping
    public void deleteCategory(@Valid @RequestBody CategoryDto categoryDto) {

    }

    @PatchMapping("/{catId}")
    public CategoryDto updateCategory(
            @Valid @RequestBody CategoryDto categoryDto,
            @PathVariable Long catId) {
        return null;
    }


}
