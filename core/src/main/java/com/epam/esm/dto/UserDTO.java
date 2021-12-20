package com.epam.esm.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

@Data
public class UserDTO {
    private Integer id;
    private String username;
    private String password;

    public UserDTO(Integer id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public UserDTO(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public UserDTO() {
    }

    public Integer getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }
}
