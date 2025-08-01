package ru.solution.test_task_for_gitflic_team.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import ru.solution.test_task_for_gitflic_team.mapper.UserMapper;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;
import ru.solution.test_task_for_gitflic_team.dto.UserDto;
import ru.solution.test_task_for_gitflic_team.dto.UserResponseDto;
import ru.solution.test_task_for_gitflic_team.entity.User;
import ru.solution.test_task_for_gitflic_team.service.UserService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final UserService userService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDto register(@RequestBody @Valid UserDto dto) {
        log.info("Attempt to register new user with username: {}", dto.username());
        User registeredUser = userService.register(dto.username(), dto.password());
        log.info("User registered successfully with ID: {}", registeredUser.getId());
        return UserMapper.toDto(registeredUser);
    }

    @PostMapping("/login")
    public UserResponseDto login(@RequestBody @Valid UserDto dto) {
        log.info("Login attempt for user: {}", dto.username());
        User user = userService.authenticate(dto.username(), dto.password());
        user.setPassword(null);
        log.info("User {} successfully authenticated", dto.username());
        return UserMapper.toDto(user);
    }

}