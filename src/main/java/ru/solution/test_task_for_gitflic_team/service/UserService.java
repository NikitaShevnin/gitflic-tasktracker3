package ru.solution.test_task_for_gitflic_team.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.solution.test_task_for_gitflic_team.entity.User;

public interface UserService extends UserDetailsService {
    User register(String username, String password);
    User authenticate(String username, String password);
}
