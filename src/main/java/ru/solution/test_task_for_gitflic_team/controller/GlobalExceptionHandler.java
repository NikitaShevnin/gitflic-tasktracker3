package ru.solution.test_task_for_gitflic_team.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ru.solution.test_task_for_gitflic_team.dto.ErrorResponse;

import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleIllegalArgument(IllegalArgumentException ex) {
        return new ErrorResponse(ex.getMessage());
    }

    @ExceptionHandler({NoSuchElementException.class, UsernameNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFound(Exception ex) {
        return new ErrorResponse(ex.getMessage());
    }
}