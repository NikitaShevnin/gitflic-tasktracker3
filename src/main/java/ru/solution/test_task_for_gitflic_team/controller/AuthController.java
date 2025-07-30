package ru.solution.test_task_for_gitflic_team.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.solution.test_task_for_gitflic_team.dto.UserDto;
import ru.solution.test_task_for_gitflic_team.entities.User;
import ru.solution.test_task_for_gitflic_team.service.UserService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public User register(@RequestBody @Valid UserDto dto) {
        return userService.register(dto.username(), dto.password());
    }
}