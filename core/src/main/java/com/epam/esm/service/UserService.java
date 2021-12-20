package com.epam.esm.service;

import com.epam.esm.dao.UserRepository;
import com.epam.esm.dto.UserDTO;
import com.epam.esm.entity.Role;
import com.epam.esm.entity.User;
import com.epam.esm.exception.DuplicateResourceException;
import com.epam.esm.exception.ResourceExistenceException;
import com.epam.esm.validator.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService implements com.epam.esm.service.Service<User> {

    private final UserRepository userRepository;
    private final UserValidator userValidator;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, UserValidator userValidator, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userValidator = userValidator;
        this.passwordEncoder = passwordEncoder;
    }

    public Integer create(UserDTO userDTO) {
        Optional<User> optionalUser = userRepository.getByUsername(userDTO.getUsername());
        if (optionalUser.isPresent()) {
            throw new DuplicateResourceException("The user with name '" +
                    userDTO.getUsername() + "' already exist", 7777);
        }
        userValidator.validate(userDTO);

        List<Role> roles = Collections.singletonList(new Role("ROLE_USER"));

        User user = toUser(userDTO);
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setRoles(roles);

        return userRepository.create(user);
    }

    private User toUser(UserDTO userDTO) {
        User user = new User();

        user.setUsername(userDTO.getUsername());

        return user;
    }

    public List<User> getAll(int limit, int offset) {
        validatePaginationParams(limit, offset);
        return userRepository.getAll(limit, offset);
    }

    public User getById(Integer id) {
        Optional<User> user = userRepository.getById(id);
        return user.orElseThrow(
                () -> new ResourceExistenceException("User with id = '" + id + "' doesn't exist", 777));
    }

    public void deleteById(Integer id) {
        Optional<User> userOptional = userRepository.getById(id);
        User user = userOptional.orElseThrow(
                () -> new ResourceExistenceException("User with id = '" + id + "' doesn't exist", 777));
        userRepository.deleteById(user);
    }

    public User getByUsername(String username) {
        Optional<User> userOptional = userRepository.getByUsername(username);
        return userOptional.orElseThrow(
                () -> new ResourceExistenceException("User with username '" + username + "' doesn't exist", 777)
        );
    }
}
