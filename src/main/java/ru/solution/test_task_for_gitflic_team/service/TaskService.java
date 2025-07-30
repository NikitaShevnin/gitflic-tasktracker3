package ru.solution.test_task_for_gitflic_team.service;

import lombok.RequiredArgsConstructor;
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

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    @Cacheable("tasks")
    public List<Task> findAll() {
        return taskRepository.findAll();
    }

    @Cacheable(value = "task", key = "#id")
    public Task findById(Long id) {
        return taskRepository.findById(id).orElseThrow();
    }

    @Transactional
    @CacheEvict(value = {"tasks", "task"}, allEntries = true)
    public Task create(Task task, User creator, Set<Long> assigneeIds) {
        task.setCreator(creator);
        if (assigneeIds != null) {
            Set<User> users = Set.copyOf(userRepository.findAllById(assigneeIds));
            task.setAssignees(users);
        }
        return taskRepository.save(task);
    }

    @Transactional
    @CacheEvict(value = {"tasks", "task"}, key = "#id", allEntries = true)
    public Task update(Long id, Task updated, User requester) {
        Task task = findById(id);
        if (!task.getCreator().getId().equals(requester.getId())) {
            throw new IllegalArgumentException(Errors.ONLY_CREATOR_UPDATE);
        }
        task.setTitle(updated.getTitle());
        task.setDescription(updated.getDescription());
        return taskRepository.save(task);
    }

    @Transactional
    @CacheEvict(value = {"tasks", "task"}, key = "#id", allEntries = true)
    public void delete(Long id, User requester) {
        Task task = findById(id);
        if (!task.getCreator().getId().equals(requester.getId())) {
            throw new IllegalArgumentException(Errors.ONLY_CREATOR_DELETE);
        }
        taskRepository.delete(task);
    }

    @Transactional
    @CacheEvict(value = {"tasks", "task"}, key = "#id", allEntries = true)
    public Task changeStatus(Long id, TaskStatus status, User requester) {
        Task task = findById(id);
        if (!task.getCreator().getId().equals(requester.getId())) {
            throw new IllegalArgumentException(Errors.ONLY_CREATOR_STATUS);
        }
        validateTransition(task.getStatus(), status);
        task.setStatus(status);
        return taskRepository.save(task);
    }

    private void validateTransition(TaskStatus from, TaskStatus to) {
        switch (from) {
            case NEW -> {
                if (to != TaskStatus.IN_PROGRESS && to != TaskStatus.COMPLETED) {
                    throw new IllegalArgumentException(Errors.INVALID_STATUS_CHANGE);
                }
            }
            case IN_PROGRESS -> {
                if (to != TaskStatus.PAUSED && to != TaskStatus.COMPLETED) {
                    throw new IllegalArgumentException(Errors.INVALID_STATUS_CHANGE);
                }
            }
            case PAUSED -> {
                if (to != TaskStatus.IN_PROGRESS && to != TaskStatus.COMPLETED) {
                    throw new IllegalArgumentException(Errors.INVALID_STATUS_CHANGE);
                }
            }
            case COMPLETED -> {
                if (to != TaskStatus.COMPLETED) {
                    throw new IllegalArgumentException(Errors.COMPLETED_CANNOT_CHANGE);
                }
            }
        }
    }
}