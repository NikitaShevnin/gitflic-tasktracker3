package ru.solution.test_task_for_gitflic_team.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.solution.test_task_for_gitflic_team.entity.Task;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, UUID> {
    @Query("select distinct t from Task t join fetch t.creator left join fetch t.assignees")
    List<Task> findAllWithUsers();

    @Query("select t from Task t join fetch t.creator left join fetch t.assignees where t.id = :id")
    Optional<Task> findByIdWithUsers(@Param("id") UUID id);
}