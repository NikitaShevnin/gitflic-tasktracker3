package ru.solution.test_task_for_gitflic_team.service;

import ru.solution.test_task_for_gitflic_team.entity.Task;
import ru.solution.test_task_for_gitflic_team.entity.TaskStatus;
import ru.solution.test_task_for_gitflic_team.entity.User;
import ru.solution.test_task_for_gitflic_team.dto.TaskResponseDto;

import java.util.List;
import java.util.Set;

public interface TaskService {
    List<TaskResponseDto> findAll();

    TaskResponseDto findById(java.util.UUID id);

    TaskResponseDto create(Task task, User creator, Set<java.util.UUID> assigneeIds);

    TaskResponseDto update(java.util.UUID id, Task updated, User requester);

    void delete(java.util.UUID id, User requester);

    TaskResponseDto changeStatus(java.util.UUID id, TaskStatus status, User requester);
}
