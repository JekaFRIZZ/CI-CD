package com.epam.esm.controller;

import com.epam.esm.dto.AuthenticationResponseDTO;
import com.epam.esm.dto.UserDTO;
import com.epam.esm.dto.security.AuthenticationRequestDTO;
import com.epam.esm.entity.User;
import com.epam.esm.security.JwtTokenProvider;
import com.epam.esm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping( "/users")
public class UserController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public UserController(UserService userService,
                          AuthenticationManager authenticationManager,
                          JwtTokenProvider jwtTokenProvider) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestHeader Map<String, String> headers) {
        String authenticationHeader = headers.get("credentials");
        if(authenticationHeader == null || authenticationHeader.isEmpty()) {
            throw new IllegalArgumentException("Credentials are empty");
        }
        AuthenticationRequestDTO requestDTO = getCredentials(authenticationHeader);

        String username = requestDTO.getUsername();
        String password = requestDTO.getPassword();
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

        User user = userService.getByUsername(username);
        String token = jwtTokenProvider.createToken(user);

        AuthenticationResponseDTO responseDto = new AuthenticationResponseDTO(username, token);

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    private AuthenticationRequestDTO getCredentials(String authenticationHeader) {
        byte[] credentialsBase64 = Base64.getDecoder().decode(authenticationHeader);
        String credentialsString = new String(credentialsBase64, StandardCharsets.UTF_8);
        String[] credentials = credentialsString.split("\\s+");

        return new AuthenticationRequestDTO(credentials[0], credentials[1]);
    }

    @PostMapping("/logout")
    /*@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")*/
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        String token = jwtTokenProvider.resolveToken(request);
        jwtTokenProvider.expire(token);
        SecurityContextLogoutHandler securityContextLogoutHandler = new SecurityContextLogoutHandler();
        securityContextLogoutHandler.logout(request, response, null);
    }

    /**
     * Returns all {@link User} .
     *
     * @param limit - a number of {@link User} objects to return
     * @param offset - a number of {@link User} objects to skip when returning
     * @return {@link ResponseEntity} with a {@link HttpStatus} and a {@link List} of {@link User} objects
     */
    @GetMapping
    public ResponseEntity<?> getAll(@RequestParam int limit,
                                    @RequestParam int offset) {
        List<User> users = userService.getAll(limit, offset);
        for(User user : users) {
            user.add(linkTo(methodOn(UserController.class).getById(user.getId())).withSelfRel());
        }
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    /**
     * Returns {@link User} object
     *
     * @param id - the {@link User} object's id that is to be retrieved from a database.
     * @return {@link ResponseEntity} with a {@link HttpStatus} and a {@link User} object or a {@link com.epam.esm.entity.ErrorData} object
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Integer id) {
        User user = userService.getById(id);
        user.add(linkTo(methodOn(UserController.class).getById(id)).withSelfRel());
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestHeader Map<String, String> headers) {
        String authenticationHeader = headers.get("credentials");
        if(authenticationHeader == null || authenticationHeader.isEmpty()) {
            throw new IllegalArgumentException("Credentials are empty");
        }
        UserDTO userDTO = getUserDTO(authenticationHeader);

        Integer userId = userService.create(userDTO);
        User user = userService.getById(userId);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    /**
     * Creates {@link User} object or throws {@link com.epam.esm.exception.FieldExistenceException} if some fields
     *      * required for creation are missing.
     *
     * @param userDTO - dto object which will be mapped to {@link User}
     * @return {@link ResponseEntity} with a {@link HttpStatus} alone or additionally with a {@link com.epam.esm.entity.ErrorData} object.
     */
    /*@PostMapping
    public ResponseEntity<?> create(@RequestBody UserDTO userDTO) {
        Integer userId = userService.create(userDTO);
        User user = userService.getById(userId);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }*/

    /**
     * Deletes {@link User} by its id or throws {@link com.epam.esm.exception.ResourceExistenceException} if the object
     * with such id doesn't exist
     *
     * @param id`s object {@link User} which will be deleted
     * @return @link ResponseEntity} with a {@link HttpStatus} alone or additionally with a {@link com.epam.esm.entity.ErrorData} object.
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> deleteById(@PathVariable Integer id) {
        userService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    private UserDTO getUserDTO(String authenticationHeader) {
        byte[] credentialsBase64 = Base64.getDecoder().decode(authenticationHeader);
        String credentialsString = new String(credentialsBase64, StandardCharsets.UTF_8);
        String[] credentials = credentialsString.split("\\s+");

        return new UserDTO(credentials[0], credentials[1]);
    }
}