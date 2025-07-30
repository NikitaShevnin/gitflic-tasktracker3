package ru.solution.test_task_for_gitflic_team.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.solution.test_task_for_gitflic_team.entities.Task;
import ru.solution.test_task_for_gitflic_team.entities.TaskStatus;
import ru.solution.test_task_for_gitflic_team.entities.User;
import ru.solution.test_task_for_gitflic_team.repository.TaskRepository;
import ru.solution.test_task_for_gitflic_team.repository.UserRepository;
import ru.solution.test_task_for_gitflic_team.errors.Errors;

import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    @Cacheable("tasks")
    public List<Task> findAll() {
        log.debug("Fetching all tasks from repository");
        List<Task> tasks = taskRepository.findAll();
        log.info("Found {} tasks in total", tasks.size());
        return tasks;
    }

    @Cacheable(value = "task", key = "#id")
    public Task findById(Long id) {
        log.debug("Looking for task with ID: {}", id);
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Task not found with ID: {}", id);
                    return new IllegalArgumentException(Errors.TASK_NOT_FOUND);
                });
        log.debug("Found task: ID={}, Title={}", task.getId(), task.getTitle());
        return task;
    }

    @Transactional
    @CacheEvict(value = {"tasks", "task"}, allEntries = true)
    public Task create(Task task, User creator, Set<Long> assigneeIds) {
        log.info("Creating new task by user ID: {}", creator.getId());
        log.debug("Task details - Title: {}, Assignees IDs: {}", task.getTitle(), assigneeIds);
        
        task.setCreator(creator);
        if (assigneeIds != null) {
            Set<User> users = Set.copyOf(userRepository.findAllById(assigneeIds));
            log.debug("Found {} assignees for task", users.size());
            task.setAssignees(users);
        }
        
        Task savedTask = taskRepository.save(task);
        log.info("Task created successfully with ID: {}", savedTask.getId());
        return savedTask;
    }

    @Transactional
    @CacheEvict(value = {"tasks", "task"}, key = "#id", allEntries = true)
    public Task update(Long id, Task updated, User requester) {
        log.info("Updating task ID: {} by user ID: {}", id, requester.getId());
        
        Task task = findById(id);
        if (!task.getCreator().getId().equals(requester.getId())) {
            log.warn("User ID {} attempted to update task they didn't create", requester.getId());
            throw new IllegalArgumentException(Errors.ONLY_CREATOR_UPDATE);
        }
        
        log.debug("Updating task fields - Old title: {}, New title: {}", 
                task.getTitle(), updated.getTitle());
        task.setTitle(updated.getTitle());
        task.setDescription(updated.getDescription());
        
        Task savedTask = taskRepository.save(task);
        log.info("Task ID: {} updated successfully", id);
        return savedTask;
    }

    @Transactional
    @CacheEvict(value = {"tasks", "task"}, key = "#id", allEntries = true)
    public void delete(Long id, User requester) {
        log.info("Deleting task ID: {} by user ID: {}", id, requester.getId());
        
        Task task = findById(id);
        if (!task.getCreator().getId().equals(requester.getId())) {
            log.warn("User ID {} attempted to delete task they didn't create", requester.getId());
            throw new IllegalArgumentException(Errors.ONLY_CREATOR_DELETE);
        }
        
        taskRepository.delete(task);
        log.info("Task ID: {} deleted successfully", id);
    }

    @Transactional
    @CacheEvict(value = {"tasks", "task"}, key = "#id", allEntries = true)
    public Task changeStatus(Long id, TaskStatus status, User requester) {
        log.info("Changing status for task ID: {} to {} by user ID: {}", 
                id, status, requester.getId());
        
        Task task = findById(id);
        if (!task.getCreator().getId().equals(requester.getId())) {
            log.warn("User ID {} attempted to change status of task they didn't create", requester.getId());
            throw new IllegalArgumentException(Errors.ONLY_CREATOR_STATUS);
        }
        
        log.debug("Current task status: {}, Requested status: {}", task.getStatus(), status);
        validateTransition(task.getStatus(), status);
        
        task.setStatus(status);
        Task savedTask = taskRepository.save(task);
        log.info("Status changed successfully for task ID: {}", id);
        return savedTask;
    }

    private void validateTransition(TaskStatus from, TaskStatus to) {
        log.debug("Validating status transition from {} to {}", from, to);
        
        switch (from) {
            case NEW -> {
                if (to != TaskStatus.IN_PROGRESS && to != TaskStatus.COMPLETED) {
                    log.error("Invalid status change from {} to {}", from, to);
                    throw new IllegalArgumentException(Errors.INVALID_STATUS_CHANGE);
                }
            }
            case IN_PROGRESS -> {
                if (to != TaskStatus.PAUSED && to != TaskStatus.COMPLETED) {
                    log.error("Invalid status change from {} to {}", from, to);
                    throw new IllegalArgumentException(Errors.INVALID_STATUS_CHANGE);
                }
            }
            case PAUSED -> {
                if (to != TaskStatus.IN_PROGRESS && to != TaskStatus.COMPLETED) {
                    log.error("Invalid status change from {} to {}", from, to);
                    throw new IllegalArgumentException(Errors.INVALID_STATUS_CHANGE);
                }
            }
            case COMPLETED -> {
                if (to != TaskStatus.COMPLETED) {
                    log.error("Attempt to change status from COMPLETED to {}", to);
                    throw new IllegalArgumentException(Errors.COMPLETED_CANNOT_CHANGE);
                }
            }
        }
    }
}