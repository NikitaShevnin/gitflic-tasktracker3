package ru.solution.test_task_for_gitflic_team.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.solution.test_task_for_gitflic_team.entities.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
}
