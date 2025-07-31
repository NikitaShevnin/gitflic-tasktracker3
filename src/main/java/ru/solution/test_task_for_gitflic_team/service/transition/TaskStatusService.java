package ru.solution.test_task_for_gitflic_team.service.transition;

import ru.solution.test_task_for_gitflic_team.entity.TaskStatus;

public interface TaskStatusService {
    void validateTransition(TaskStatus from, TaskStatus to);
}
