package ru.solution.test_task_for_gitflic_team.dto;

import ru.solution.test_task_for_gitflic_team.entities.Task;

public class TaskResponseDto {
    private Long id;
    private String title;
    private String description;
    private String status;
    private String creatorUsername;

    public TaskResponseDto(Task task) {
        this.id = task.getId();
        this.title = task.getTitle();
        this.description = task.getDescription();
        this.status = task.getStatus().name();
        this.creatorUsername = task.getCreator() != null ? task.getCreator().getUsername() : null;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getStatus() {
        return status;
    }

    public String getCreatorUsername() {
        return creatorUsername;
    }
}