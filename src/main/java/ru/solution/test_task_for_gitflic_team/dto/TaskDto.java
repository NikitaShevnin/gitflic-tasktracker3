package ru.solution.test_task_for_gitflic_team.dto;

import jakarta.validation.constraints.NotBlank;
import java.util.Set;
import java.util.UUID;

public record TaskDto(@NotBlank String title,
                      String description,
                      Set<UUID> assignees) {}