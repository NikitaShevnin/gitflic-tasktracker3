package ru.solution.test_task_for_gitflic_team.exception;

public final class Exception {
    private Exception() {}

    public static final String USER_NOT_FOUND = "User not found";
    public static final String USER_EXISTS = "User already exists";
    public static final String ONLY_CREATOR_UPDATE = "Only creator can update";
    public static final String ONLY_CREATOR_DELETE = "Only creator can delete";
    public static final String ONLY_CREATOR_STATUS = "Only creator can change status";
    public static final String INVALID_STATUS_CHANGE = "Invalid status change";
    public static final String COMPLETED_CANNOT_CHANGE = "Completed tasks cannot change";
    public static final String TASK_NOT_FOUND = "Task not found";
}