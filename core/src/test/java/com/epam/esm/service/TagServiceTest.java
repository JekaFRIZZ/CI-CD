package com.epam.esm.service;

import com.epam.esm.dao.TagRepository;
import com.epam.esm.dto.TagDTO;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.DuplicateResourceException;
import com.epam.esm.exception.ResourceExistenceException;
import com.epam.esm.validator.TagValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.mockito.Mockito.*;

class TagServiceTest {

    private final TagRepository mockTagRepository = mock(TagRepository.class);
    private final TagValidator mockTagValidator = mock(TagValidator.class);
    private final TagService tagService = new TagService(mockTagRepository, mockTagValidator);

    private final List<Tag> tags = new ArrayList<>();
    private final Tag tag = new Tag();
    private final TagDTO tagDTO = new TagDTO();
    private final Integer tagId = 1;
    private final String name = "cat";
    private final Optional<Tag> optionalTag = Optional.of(tag);

    @Test
    void testShouldGetAllTagsWhenCorrectPaginateParamApplied() {
        int limit = 5;
        int offset = 1;

        when(mockTagRepository.getAll(limit, offset)).thenReturn(tags);

        List<Tag> actual = tagService.getAll(limit, offset);

        Assertions.assertEquals(tags, actual);
    }

    @Test
    void testShouldThrowExceptionWhenIncorrectPaginateParamApplied() {
        int limit = -5;
        int offset = -5;
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            tagService.getAll(limit, offset);
        });
    }

    @Test
    void testShouldGetTagById() {
        when(mockTagRepository.getById(tagId)).thenReturn(optionalTag);

        Tag actual = tagService.getById(tagId);

        Assertions.assertEquals(tag, actual);
    }

    @Test
    void testShouldThrowExceptionWhenNonExistentTagApplied() {
        when(mockTagRepository.getById(tagId)).thenReturn(Optional.empty());
        Assertions.assertThrows(ResourceExistenceException.class, () -> {
            tagService.getById(tagId);
        });
    }

    @Test
    void testShouldGetTagByName() {
        when(mockTagRepository.getByName(name)).thenReturn(optionalTag);

        Tag actual = tagService.getByName(name);

        Assertions.assertEquals(tag, actual);
    }

    @Test
    void testGetByNameShouldThrowExceptionWhenNonExistentTagApplied() {
        when(mockTagRepository.getByName(name)).thenReturn(Optional.empty());
        Assertions.assertThrows(ResourceExistenceException.class, () -> {
            tagService.getByName(name);
        });
    }

    @Test
    void testShouldReturnTagIdWhenCreateTag() {
        tagDTO.setName(name);
        doNothing().when(mockTagValidator).validate(tagDTO);
        when(mockTagRepository.getByName(name)).thenReturn(Optional.empty());
        when(tagService.create(tagDTO)).thenReturn(1);
        when(mockTagRepository.create(tag)).thenReturn(tagId);

        Integer actual = tagService.create(tagDTO);

        Assertions.assertEquals(tagId, actual);
    }

    @Test
    void testShouldThrowExceptionWhenTagWithSameNameExistsApplied() {
        tagDTO.setName(name);
        when(mockTagRepository.getByName(name)).thenReturn(Optional.of(tag));
        Assertions.assertThrows(DuplicateResourceException.class, () -> {
            tagService.create(tagDTO);
        });
    }

    @Test
    void testDeleteByIdShouldThrowExceptionWhenDeleteNonExistentTag() {
        when(mockTagRepository.getById(tagId)).thenReturn(Optional.empty());
        Assertions.assertThrows(ResourceExistenceException.class, () -> {
            tagService.deleteById(tagId);
        });
    }
}