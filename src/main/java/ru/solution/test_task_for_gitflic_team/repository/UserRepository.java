package ru.solution.test_task_for_gitflic_team.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.solution.test_task_for_gitflic_team.entity.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
}
