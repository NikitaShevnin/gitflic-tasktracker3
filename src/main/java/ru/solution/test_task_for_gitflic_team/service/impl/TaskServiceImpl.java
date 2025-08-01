package ru.solution.test_task_for_gitflic_team.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.solution.test_task_for_gitflic_team.entity.Task;
import ru.solution.test_task_for_gitflic_team.entity.TaskStatus;
import ru.solution.test_task_for_gitflic_team.entity.User;
import ru.solution.test_task_for_gitflic_team.mapper.TaskMapper;
import ru.solution.test_task_for_gitflic_team.dto.TaskResponseDto;
import ru.solution.test_task_for_gitflic_team.repository.TaskRepository;
import ru.solution.test_task_for_gitflic_team.repository.UserRepository;
import ru.solution.test_task_for_gitflic_team.exception.Exception;
import jakarta.persistence.EntityNotFoundException;
import ru.solution.test_task_for_gitflic_team.service.transition.TaskStatusService;
import ru.solution.test_task_for_gitflic_team.service.TaskService;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final TaskStatusService statusService;

    @Cacheable("tasks")
    @Transactional(readOnly = true)
    public List<TaskResponseDto> findAll() {
        log.debug("Fetching all tasks from repository with users");
        List<Task> tasks = taskRepository.findAllWithUsers();
        log.info("Found {} tasks in total with users loaded", tasks.size());
        return tasks.stream()
                .map(TaskMapper::toDto)
                .toList();
    }

    @Cacheable(value = "task", key = "#id")
    @Transactional(readOnly = true)
    public TaskResponseDto findById(UUID id) {
        log.debug("Looking for task with ID: {} with users", id);
        Task task = taskRepository.findByIdWithUsers(id)
                .orElseThrow(() -> {
                    log.error("Task not found with ID: {}", id);
                    return new EntityNotFoundException(Exception.TASK_NOT_FOUND);
                });
        return TaskMapper.toDto(task);
    }

    @Transactional
    @CacheEvict(value = {"tasks", "task"}, allEntries = true)
    public TaskResponseDto create(Task task, User creator, Set<UUID> assigneeIds) {
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
        return TaskMapper.toDto(savedTask);
    }

    @Transactional
    @CacheEvict(value = {"tasks", "task"}, key = "#id", allEntries = true)
    public TaskResponseDto update(UUID id, Task updated, User requester) {
        log.info("Updating task ID: {} by user ID: {}", id, requester.getId());
        
        Task task = taskRepository.findByIdWithUsers(id)
                .orElseThrow(() -> {
                    log.error("Task not found with ID: {}", id);
                    return new EntityNotFoundException(Exception.TASK_NOT_FOUND);
                });
        if (!task.getCreator().getId().equals(requester.getId())) {
            log.warn("User ID {} attempted to update task they didn't create", requester.getId());
            throw new IllegalArgumentException(Exception.ONLY_CREATOR_UPDATE);
        }
        
        log.debug("Updating task fields - Old title: {}, New title: {}", 
                task.getTitle(), updated.getTitle());
        task.setTitle(updated.getTitle());
        task.setDescription(updated.getDescription());
        
        Task savedTask = taskRepository.save(task);
        log.info("Task ID: {} updated successfully", id);
        return TaskMapper.toDto(savedTask);
    }

    @Transactional
    @CacheEvict(value = {"tasks", "task"}, key = "#id", allEntries = true)
    public void delete(UUID id, User requester) {
        log.info("Deleting task ID: {} by user ID: {}", id, requester.getId());
        
        Task task = taskRepository.findByIdWithUsers(id)
                .orElseThrow(() -> {
                    log.error("Task not found with ID: {}", id);
                    return new EntityNotFoundException(Exception.TASK_NOT_FOUND);
                });
        if (!task.getCreator().getId().equals(requester.getId())) {
            log.warn("User ID {} attempted to delete task they didn't create", requester.getId());
            throw new IllegalArgumentException(Exception.ONLY_CREATOR_DELETE);
        }
        
        taskRepository.delete(task);
        log.info("Task ID: {} deleted successfully", id);
    }

    @Transactional
    @CacheEvict(value = {"tasks", "task"}, key = "#id", allEntries = true)
    public TaskResponseDto changeStatus(UUID id, TaskStatus status, User requester) {
        log.info("Changing status for task ID: {} to {} by user ID: {}", 
                id, status, requester.getId());
        
        Task task = taskRepository.findByIdWithUsers(id)
                .orElseThrow(() -> {
                    log.error("Task not found with ID: {}", id);
                    return new EntityNotFoundException(Exception.TASK_NOT_FOUND);
                });
        if (!task.getCreator().getId().equals(requester.getId())) {
            log.warn("User ID {} attempted to change status of task they didn't create", requester.getId());
            throw new IllegalArgumentException(Exception.ONLY_CREATOR_STATUS);
        }
        
        log.debug("Current task status: {}, Requested status: {}", task.getStatus(), status);
        statusService.validateTransition(task.getStatus(), status);
        
        task.setStatus(status);
        Task savedTask = taskRepository.save(task);
        log.info("Status changed successfully for task ID: {}", id);
        return TaskMapper.toDto(savedTask);
    }

}