package com.epam.esm.dto.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

@Data
@AllArgsConstructor
public class AuthenticationRequestDTO {
    private String username;
    private String password;
}
