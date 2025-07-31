package ru.solution.test_task_for_gitflic_team.service;

import ru.solution.test_task_for_gitflic_team.entity.Task;
import ru.solution.test_task_for_gitflic_team.entity.TaskStatus;
import ru.solution.test_task_for_gitflic_team.entity.User;
import ru.solution.test_task_for_gitflic_team.dto.TaskResponseDto;

import java.util.List;
import java.util.Set;

public interface TaskService {
    List<TaskResponseDto> findAll();

    TaskResponseDto findById(Long id);

    TaskResponseDto create(Task task, User creator, Set<Long> assigneeIds);

    TaskResponseDto update(Long id, Task updated, User requester);

    void delete(Long id, User requester);

    TaskResponseDto changeStatus(Long id, TaskStatus status, User requester);
}
