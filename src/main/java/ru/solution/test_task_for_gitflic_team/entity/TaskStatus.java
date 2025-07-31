package ru.solution.test_task_for_gitflic_team.entity;

import java.util.EnumSet;
import java.util.Set;

public enum TaskStatus {
    NEW,
    IN_PROGRESS,
    PAUSED,
    COMPLETED;

    private Set<TaskStatus> allowedTransitions;

    static {
        NEW.allowedTransitions = EnumSet.of(IN_PROGRESS, COMPLETED);
        IN_PROGRESS.allowedTransitions = EnumSet.of(PAUSED, COMPLETED);
        PAUSED.allowedTransitions = EnumSet.of(IN_PROGRESS, COMPLETED);
        COMPLETED.allowedTransitions = EnumSet.of(COMPLETED);
    }

    public Set<TaskStatus> getAllowedTransitions() {
        return allowedTransitions;
    }
}
