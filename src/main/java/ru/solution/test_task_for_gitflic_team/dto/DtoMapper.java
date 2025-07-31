package ru.solution.test_task_for_gitflic_team.dto;

import ru.solution.test_task_for_gitflic_team.entities.Task;
import ru.solution.test_task_for_gitflic_team.entities.User;

public final class DtoMapper {
    private DtoMapper() {}

    public static UserResponseDto toDto(User user) {
        return new UserResponseDto(user.getId(), user.getUsername());
    }

    public static TaskResponseDto toDto(Task task) {
        return new TaskResponseDto(task);
    }
}
    