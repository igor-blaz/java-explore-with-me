package ru.practicum.publicApi;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.category.CategoryDto;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/categories")
@RequiredArgsConstructor
public class PublicCategoriesController {
    @GetMapping
    public List<CategoryDto> getCategories(
            @RequestParam(defaultValue = "0") int from,
            @RequestParam(defaultValue = "10") int size
    ) {
        return null;
    }

    @GetMapping("/{catId}")
    public List<CategoryDto> getCategoryById(@PathVariable Long catId){
        return null;
    }

}
