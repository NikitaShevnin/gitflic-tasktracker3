package ru.solution.test_task_for_gitflic_team.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.solution.test_task_for_gitflic_team.entities.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {
}