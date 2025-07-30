package ru.solution.test_task_for_gitflic_team.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.solution.test_task_for_gitflic_team.dto.TaskDto;
import ru.solution.test_task_for_gitflic_team.entities.Task;
import ru.solution.test_task_for_gitflic_team.entities.TaskStatus;
import ru.solution.test_task_for_gitflic_team.entities.User;
import ru.solution.test_task_for_gitflic_team.service.TaskService;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;

    @GetMapping
    public List<Task> all() {
        return taskService.findAll();
    }

    @GetMapping("/{id}")
    public Task get(@PathVariable Long id) {
        return taskService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Task create(@RequestBody @Valid TaskDto dto,
                       @AuthenticationPrincipal User creator) {
        Task task = new Task();
        task.setTitle(dto.title());
        task.setDescription(dto.description());
        return taskService.create(task, creator, dto.assignees());
    }

    @PutMapping("/{id}")
    public Task update(@PathVariable Long id,
                       @RequestBody @Valid TaskDto dto,
                       @AuthenticationPrincipal User creator) {
        Task updated = new Task();
        updated.setTitle(dto.title());
        updated.setDescription(dto.description());
        return taskService.update(id, updated, creator);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id, @AuthenticationPrincipal User user) {
        taskService.delete(id, user);
    }

    @PostMapping("/{id}/status")
    public Task changeStatus(@PathVariable Long id,
                             @RequestParam TaskStatus status,
                             @AuthenticationPrincipal User user) {
        return taskService.changeStatus(id, status, user);
    }
}