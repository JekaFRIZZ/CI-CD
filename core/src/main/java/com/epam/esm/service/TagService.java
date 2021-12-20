package com.epam.esm.service;


import com.epam.esm.dao.TagRepository;
import com.epam.esm.dto.TagDTO;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.DuplicateResourceException;
import com.epam.esm.exception.ResourceExistenceException;
import com.epam.esm.validator.TagValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TagService implements com.epam.esm.service.Service<Tag> {

    private final TagRepository tagRepository;
    private final TagValidator validator;

    @Autowired
    public TagService(TagRepository tagRepository, TagValidator validator) {
        this.tagRepository = tagRepository;
        this.validator = validator;
    }

    public List<Tag> getAll(int limit, int offset) {
        validatePaginationParams(limit, offset);
        return tagRepository.getAll(limit, offset);
    }

    public Tag getById(Integer id) throws ResourceExistenceException {
        Optional<Tag> tag = tagRepository.getById(id);

        return tag.orElseThrow(
                (() -> new ResourceExistenceException("Tag with id = '" + id + "' doesn't exist", 777)));
    }

    public Tag getByName(String name) throws ResourceExistenceException {
        Optional<Tag> tag = tagRepository.getByName(name);

        return tag.orElseThrow(
                (() -> new ResourceExistenceException("Tag with name '" + name + "' doesn't exist", 777)));
    }

    @Transactional
    public Integer create(TagDTO tagDTO) {
        validator.validate(tagDTO);

        Tag tag = toTag(tagDTO);
        Optional<Tag> tagOptional = tagRepository.getByName(tag.getName());

        if (tagOptional.isPresent()) {
            throw new DuplicateResourceException("The tag with name '" + tagDTO.getName() + "' already exist", 7777);
        }

        return tagRepository.create(tag);
    }

    @Transactional
    public void deleteById(Integer id) {
        Optional<Tag> tagOptional = tagRepository.getById(id);
        Tag tag = tagOptional.orElseThrow(
                () -> new ResourceExistenceException("Tag with id ='" + id + "' doesn't exist", 777)
        );

        tagRepository.deleteById(tag);
    }

    public void updateTags(List<Tag> tags) {
        for (int i = 0; i < tags.size(); i++) {
            Optional<Tag> tag = tagRepository.getByName(tags.get(i).getName());
            if (!tag.isPresent()) {
                tagRepository.create(tags.get(i));
            } else {
                tag.get().getId();
            }
        }
    }

    private Tag toTag(TagDTO tagDTO) {
        Tag tag = new Tag();
        tag.setName(tagDTO.getName());
        return tag;
    }

    public Tag getMostWidelyUsedTagWithHighestCost() {
        return tagRepository.getMostWidelyUsedTagWithHighestCost();
    }

    public List<Tag> getPersistedTags(List<Tag> tags) {
        List<Tag> result = new ArrayList<>();

        for (Tag tag : tags) {
            Optional<Tag> tagOptional = tagRepository.getByName(tag.getName());
            Tag tagResult = tagOptional.orElse(tag);
            result.add(tagResult);
        }
        return result;
    }
}
