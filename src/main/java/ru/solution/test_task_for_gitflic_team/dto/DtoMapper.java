package ru.solution.test_task_for_gitflic_team.dto;

import ru.solution.test_task_for_gitflic_team.entities.Task;
import ru.solution.test_task_for_gitflic_team.entities.User;

import java.util.Set;
import java.util.stream.Collectors;

public final class DtoMapper {
    private DtoMapper() {}

    public static UserResponseDto toDto(User user) {
        return new UserResponseDto(user.getId(), user.getUsername());
    }

    public static TaskResponseDto toDto(Task task) {
        Long creatorId = task.getCreator() != null ? task.getCreator().getId() : null;
        Set<Long> assigneeIds = task.getAssignees().stream()
                .map(User::getId)
                .collect(Collectors.toSet());
        return new TaskResponseDto(task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                creatorId,
                assigneeIds);
    }
}
