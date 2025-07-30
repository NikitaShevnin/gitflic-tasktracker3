package ru.solution.test_task_for_gitflic_team.dto;

import java.util.Set;
import ru.solution.test_task_for_gitflic_team.entities.TaskStatus;


public record TaskResponseDto(Long id,
                              String title,
                              String description,
                              TaskStatus status,
                              Long creatorId,
                              Set<Long> assignees) {}