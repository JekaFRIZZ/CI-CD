package com.epam.esm.validator;

import com.epam.esm.dto.TagDTO;
import com.epam.esm.exception.ValidationException;
import org.springframework.stereotype.Component;

@Component
public class TagValidator implements Validator<TagDTO> {
    private static final int MIN_LENGTH_NAME = 2;
    private static final int MAX_LENGTH_NAME = 30;

    @Override
    public void validate(TagDTO entity) {
        int nameSize = entity.getName().length();

        if(nameSize <= MIN_LENGTH_NAME || nameSize >= MAX_LENGTH_NAME) {
            throw new ValidationException("The name must be between 2 and 30 characters");
        }
    }
}
