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

import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class CategoryStorage {
    private final CategoryRepository repository;

    public List<Category> getAllCategories(int from, int size) {
        return repository.findCategoriesNative(from, size);

    }

    public boolean isExistsByName(String name) {
        return repository.existsByName(name);
    }

    public boolean isExistsByNameNotInId(String name, Long id) {
        return repository.existsByNameAndIdNot(name, id);
    }

    public Category addNewCategory(NewCategoryDto newCategoryDto) {
        return repository.save(CategoryMapper.toEntity(newCategoryDto));
    }

    public Category getCategoryById(Long id) {
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
