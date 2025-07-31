package ru.solution.test_task_for_gitflic_team.mapper;

import ru.solution.test_task_for_gitflic_team.dto.UserResponseDto;
import ru.solution.test_task_for_gitflic_team.entity.User;

public final class UserMapper {
    private UserMapper() {}

    public static UserResponseDto toDto(User user) {
        return new UserResponseDto(user.getId(), user.getUsername());
    }
}
