package ru.solution.test_task_for_gitflic_team.service.transition.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.solution.test_task_for_gitflic_team.entity.TaskStatus;
import ru.solution.test_task_for_gitflic_team.exception.Exception;
import ru.solution.test_task_for_gitflic_team.service.transition.TaskStatusService;

@Slf4j
@Service
public class TaskStatusServiceImpl implements TaskStatusService {
    @Override
    public void validateTransition(TaskStatus from, TaskStatus to) {
        log.debug("Validating status transition from {} to {}", from, to);
        if (!from.getAllowedTransitions().contains(to)) {
            if (from == TaskStatus.COMPLETED) {
                log.error("Attempt to change status from COMPLETED to {}", to);
                throw new IllegalArgumentException(Exception.COMPLETED_CANNOT_CHANGE);
            }
            log.error("Invalid status change from {} to {}", from, to);
            throw new IllegalArgumentException(Exception.INVALID_STATUS_CHANGE);
        }
    }
}
