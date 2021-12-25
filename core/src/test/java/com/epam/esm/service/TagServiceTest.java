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
    public void testShouldGetAllTagsWhenCorrectPaginateParamApplied() {
        int limit = 5;
        int offset = 1;

        when(mockTagRepository.getAll(limit, offset)).thenReturn(tags);

        List<Tag> actual = tagService.getAll(limit, offset);

        Assertions.assertEquals(tags, actual);
    }

    @Test
    public void testShouldThrowExceptionWhenIncorrectPaginateParamApplied() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            int limit = -5;
            int offset = -5;

            tagService.getAll(limit, offset);
        });
    }

    @Test
    public void testShouldGetTagById() {
        when(mockTagRepository.getById(tagId)).thenReturn(optionalTag);

        Tag actual = tagService.getById(tagId);

        Assertions.assertEquals(tag, actual);
    }

    @Test
    public void testShouldThrowExceptionWhenNonExistentTagApplied() {
        Assertions.assertThrows(ResourceExistenceException.class, () -> {
            when(mockTagRepository.getById(tagId)).thenReturn(Optional.empty());

            tagService.getById(tagId);
        });
    }

    @Test
    public void testShouldGetTagByName() {
        when(mockTagRepository.getByName(name)).thenReturn(optionalTag);

        Tag actual = tagService.getByName(name);

        Assertions.assertEquals(tag, actual);
    }

    @Test
    public void testGetByNameShouldThrowExceptionWhenNonExistentTagApplied() {
        Assertions.assertThrows(ResourceExistenceException.class, () -> {
            when(mockTagRepository.getByName(name)).thenReturn(Optional.empty());

            tagService.getByName(name);
        });
    }

    @Test
    public void testShouldReturnTagIdWhenCreateTag() {
        tagDTO.setName(name);
        doNothing().when(mockTagValidator).validate(tagDTO);
        when(mockTagRepository.getByName(name)).thenReturn(Optional.empty());
        when(tagService.create(tagDTO)).thenReturn(1);
        when(mockTagRepository.create(tag)).thenReturn(tagId);

        Integer actual = tagService.create(tagDTO);

        Assertions.assertEquals(tagId, actual);
    }

    @Test
    public void testShouldThrowExceptionWhenTagWithSameNameExistsApplied() {
        Assertions.assertThrows(DuplicateResourceException.class, () -> {
            tagDTO.setName(name);
            when(mockTagRepository.getByName(name)).thenReturn(Optional.of(tag));

            tagService.create(tagDTO);
        });
    }

    @Test
    public void testShouldDoNothingWhenDeleteTagApplied() {
        when(mockTagRepository.getById(tagId)).thenReturn(optionalTag);
        doNothing().when(mockTagRepository).deleteById(tag);

        tagService.deleteById(tagId);
    }

    @Test
    public void testDeleteByIdShouldThrowExceptionWhenDeleteNonExistentTag() {
        Assertions.assertThrows(ResourceExistenceException.class, () -> {
            when(mockTagRepository.getById(tagId)).thenReturn(Optional.empty());

            tagService.deleteById(tagId);
        });
    }
}