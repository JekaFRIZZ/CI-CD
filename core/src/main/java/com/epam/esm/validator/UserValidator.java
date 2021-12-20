package com.epam.esm.validator;

import com.epam.esm.dto.UserDTO;
import com.epam.esm.exception.ValidationException;

public class UserValidator implements Validator<UserDTO> {
    @Override
    public void validate(UserDTO entity) {
        String name = entity.getUsername();

        if(name.length() < MIN_LENGTH_NAME || name.length() > MAX_LENGTH_NAME) {
            throw new ValidationException("The name must be between 2 and 30 characters");
        }
    }
}
