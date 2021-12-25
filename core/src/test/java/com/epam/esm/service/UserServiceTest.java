package com.epam.esm.service;

import com.epam.esm.dao.UserRepository;
import com.epam.esm.dto.UserDTO;
import com.epam.esm.entity.User;
import com.epam.esm.exception.ResourceExistenceException;
import com.epam.esm.validator.UserValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {
    private final UserRepository mockUserRepository = mock(UserRepository.class);
    private final UserValidator mockUserValidator = mock(UserValidator.class);
    private final BCryptPasswordEncoder mockPasswordEncoder = mock(BCryptPasswordEncoder.class);
    private final UserService userService = new UserService(mockUserRepository, mockUserValidator, mockPasswordEncoder);

    private final List<User> users = new ArrayList<>();
    private final User user = new User();
    private final UserDTO userDTO = new UserDTO();
    private final Integer userId = 1;
    private final Optional<User> optionalUser = Optional.of(user);

    @Test
    void testShouldGetAllUsersWhenCorrectPaginateParamApplied() {
        int limit = 5;
        int offset = 2;
        Mockito.when(mockUserRepository.getAll(limit, offset)).thenReturn(users);

        List<User> actual = userService.getAll(limit, offset);

        assertEquals(users, actual);
    }

    @Test
    void testShouldThrowExceptionWhenIncorrectPaginateParamApplied() {
        assertThrows(IllegalArgumentException.class, () -> {
            int limit = -5;
            int offset = -5;

            userService.getAll(limit, offset);
        });
    }

    @Test
    void testShouldGetUserById() {
        Mockito.when(mockUserRepository.getById(userId)).thenReturn(optionalUser);

        User actual = userService.getById(userId);

        assertEquals(user, actual);
    }

    @Test
    void testShouldThrowExceptionWhenNonExistentUserApplied() {
        Assertions.assertThrows(ResourceExistenceException.class, () -> {
            when(mockUserRepository.getById(userId)).thenReturn(Optional.empty());

            userService.getById(userId);
        });
    }

    @Test
    void testShouldDoNothingWhenDeleteUser() {
        when(mockUserRepository.getById(userId)).thenReturn(optionalUser);
        doNothing().when(mockUserRepository).deleteById(user);

        userService.deleteById(userId);
    }

    @Test
    void testShouldThrowExceptionWheNonExistentUserApplied() {
        Assertions.assertThrows(ResourceExistenceException.class, () -> {
            when(mockUserRepository.getById(userId)).thenReturn(Optional.empty());

            userService.deleteById(userId);
        });
    }
}