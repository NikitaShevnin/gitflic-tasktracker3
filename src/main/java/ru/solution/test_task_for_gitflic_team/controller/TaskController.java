package ru.solution.test_task_for_gitflic_team.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.solution.test_task_for_gitflic_team.dto.TaskDto;
import ru.solution.test_task_for_gitflic_team.entity.TaskStatus;
import ru.solution.test_task_for_gitflic_team.entity.User;
import ru.solution.test_task_for_gitflic_team.service.TaskService;
import ru.solution.test_task_for_gitflic_team.dto.TaskResponseDto;
import ru.solution.test_task_for_gitflic_team.entity.Task;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/task")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;

    @GetMapping
    public List<TaskResponseDto> all() {
        log.info("Requesting all tasks");
        List<TaskResponseDto> tasks = taskService.findAll();
        log.debug("Found {} tasks", tasks.size());
        return tasks;
    }

    @GetMapping("/{id}")
    public TaskResponseDto get(@PathVariable Long id) {
        log.info("Requesting task with ID: {}", id);
        TaskResponseDto task = taskService.findById(id);
        log.debug("Found task: ID={}, Title={}", id, task.getTitle());
        return task;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaskResponseDto create(@RequestBody @Valid TaskDto dto,
                                @AuthenticationPrincipal User creator) {
        log.info("Creating new task by user {} (ID: {})", creator.getUsername(), creator.getId());
        log.debug("Task details - Title: {}, Assignees: {}", dto.title(), dto.assignees());
        
        Task task = new Task();
        task.setTitle(dto.title());
        task.setDescription(dto.description());
        
        TaskResponseDto createdTask = taskService.create(task, creator, dto.assignees());
        log.info("Task created successfully with ID: {}", createdTask.getId());
        return createdTask;
    }

    @PutMapping("/{id}")
    public TaskResponseDto update(@PathVariable Long id,
                                @RequestBody @Valid TaskDto dto,
                                @AuthenticationPrincipal User creator) {
        log.info("Updating task ID: {} by user {} (ID: {})", 
                id, creator.getUsername(), creator.getId());
        log.debug("Update details - Title: {}, Description: {}", dto.title(), dto.description());
        
        Task updated = new Task();
        updated.setTitle(dto.title());
        updated.setDescription(dto.description());
        
        TaskResponseDto result = taskService.update(id, updated, creator);
        log.info("Task ID: {} updated successfully", id);
        return result;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id, @AuthenticationPrincipal User user) {
        log.info("Deleting task ID: {} by user {} (ID: {})", 
                id, user.getUsername(), user.getId());
        taskService.delete(id, user);
        log.info("Task ID: {} deleted successfully", id);
    }

    @PostMapping("/{id}/status")
    public TaskResponseDto changeStatus(@PathVariable Long id,
                                      @RequestParam TaskStatus status,
                                      @AuthenticationPrincipal User user) {
        log.info("Changing status for task ID: {} to {} by user {} (ID: {})", 
                id, status, user.getUsername(), user.getId());
        TaskResponseDto updatedTask = taskService.changeStatus(id, status, user);
        log.info("Status changed successfully for task ID: {}", id);
        return updatedTask;
    }
}