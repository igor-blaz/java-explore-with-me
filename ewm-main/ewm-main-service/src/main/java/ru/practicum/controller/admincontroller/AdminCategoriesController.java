package ru.practicum.controller.admincontroller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.dto.category.NewCategoryDto;
import ru.practicum.service.adminservice.AdminCategoryService;

@Slf4j
@RestController
@RequestMapping(path = "/admin/categories")
@RequiredArgsConstructor
public class AdminCategoriesController {

    private final AdminCategoryService categoryService;

    @PostMapping
    public CategoryDto addCategory(@Valid @RequestBody NewCategoryDto newCategoryDto) {
        return categoryService.addCategory(newCategoryDto);
    }

    @DeleteMapping("/{catId}")
    public void deleteCategory(@PathVariable Long catId) {
        categoryService.deleteCategory(catId);
    }

    @PatchMapping("/{catId}")
    public CategoryDto updateCategory(
            @Valid @RequestBody CategoryDto categoryDto,
            @PathVariable Long catId) {
        return categoryService.updateCategory(categoryDto, catId);
    }


}
