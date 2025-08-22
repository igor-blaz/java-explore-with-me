package ru.practicum.controller.admincontroller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.user.UserDto;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/admin/users")
@RequiredArgsConstructor
public class AdminUsersController {

    @GetMapping
    public List<UserDto> getUserDto(@RequestParam(required = false) List<Integer> ids,
                                    @RequestParam(defaultValue = "0") int from,
                                    @RequestParam(defaultValue = "10") int size) {
        return null;
    }

    @PostMapping
    public UserDto addUserDto(@Valid @RequestBody UserDto userDto) {
        return null;
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable Long userId) {
    }

}
