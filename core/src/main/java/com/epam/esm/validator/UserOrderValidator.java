package com.epam.esm.validator;

import com.epam.esm.dto.UserOrderDTO;
import com.epam.esm.exception.ValidationException;

import java.math.BigDecimal;

public class UserOrderValidator implements Validator<UserOrderDTO> {

    @Override
    public void validate(UserOrderDTO entity) {
        if(entity.getPrice().compareTo(BigDecimal.ZERO) < 0) {
            throw new ValidationException("The price must not be negative");
        }
    }
}
