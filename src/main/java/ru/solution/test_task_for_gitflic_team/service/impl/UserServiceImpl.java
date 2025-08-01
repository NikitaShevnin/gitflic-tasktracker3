package ru.solution.test_task_for_gitflic_team.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.solution.test_task_for_gitflic_team.entity.User;
import ru.solution.test_task_for_gitflic_team.repository.UserRepository;
import ru.solution.test_task_for_gitflic_team.exception.Exception;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) {
        log.debug("Attempting to load user by username: {}", username);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.error("User not found with username: {}", username);
                    return new EntityNotFoundException(Exception.USER_NOT_FOUND);
                });
        log.info("User loaded successfully: {}", username);
        return user;
    }

    @Transactional
    public User register(String username, String password) {
        log.info("Attempting to register new user: {}", username);
        
        if (userRepository.existsByUsername(username)) {
            log.warn("Registration failed - username already exists: {}", username);
            throw new IllegalArgumentException(Exception.USER_EXISTS);
        }
        
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        
        User registeredUser = userRepository.save(user);
        log.info("User registered successfully with ID: {}", registeredUser.getId());
        return registeredUser;
    }

    @Override
    @Transactional(readOnly = true)
    public User authenticate(String username, String password) {
        var authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return (User) authentication.getPrincipal();
    }

}