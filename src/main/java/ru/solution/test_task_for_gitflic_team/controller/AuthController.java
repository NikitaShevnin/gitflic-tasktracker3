package ru.solution.test_task_for_gitflic_team.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;
import ru.solution.test_task_for_gitflic_team.dto.DtoMapper;
import ru.solution.test_task_for_gitflic_team.dto.UserDto;
import ru.solution.test_task_for_gitflic_team.dto.UserResponseDto;
import ru.solution.test_task_for_gitflic_team.entities.User;
import ru.solution.test_task_for_gitflic_team.service.UserService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDto register(@RequestBody @Valid UserDto dto) {
        log.info("Attempt to register new user with username: {}", dto.username());
        User registeredUser = userService.register(dto.username(), dto.password());
        log.info("User registered successfully with ID: {}", registeredUser.getId());
        return DtoMapper.toDto(registeredUser);
    }

    @PostMapping("/login")
    public User login(@RequestBody @Valid UserDto dto) {
        log.info("Login attempt for user: {}", dto.username());
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(dto.username(), dto.password()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            User user = (User) authentication.getPrincipal();
            user.setPassword(null);
            log.info("User {} successfully authenticated", dto.username());
            return user;
        } catch (Exception e) {
            log.error("Authentication failed for user: {}. Reason: {}", dto.username(), e.getMessage());
            throw e;
        }
    }
}