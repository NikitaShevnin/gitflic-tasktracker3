package ru.solution.test_task_for_gitflic_team.dto;

import jakarta.validation.constraints.NotBlank;
import java.util.Set;

public record TaskDto(@NotBlank String title,
                      String description,
                      Set<Long> assignees) {}