package ru.solution.test_task_for_gitflic_team.dto;

import jakarta.validation.constraints.NotBlank;

public record UserDto(@NotBlank String username, @NotBlank String password) {}