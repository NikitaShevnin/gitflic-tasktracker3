package ru.solution.test_task_for_gitflic_team.mapper;

import ru.solution.test_task_for_gitflic_team.dto.TaskResponseDto;
import ru.solution.test_task_for_gitflic_team.entity.Task;

public final class TaskMapper {
    private TaskMapper() {}

    public static TaskResponseDto toDto(Task task) {
        return new TaskResponseDto(task);
    }
}
