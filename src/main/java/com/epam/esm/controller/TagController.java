package com.epam.esm.controller;

import com.epam.esm.dto.TagDTO;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.ResourceExistenceException;
import com.epam.esm.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.util.List;

@RestController
@RequestMapping("/tags")
public class TagController {

    private final TagService tagService;

    @Autowired
    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    /**
     * Returns all {@link Tag} from database
     *
     * @return  {@link ResponseEntity} with a {@link HttpStatus} and all {@link Tag}.
     */
    @GetMapping()
    public ResponseEntity<?> getAll(@RequestParam int limit,
                                    @RequestParam int offset) {
        List<Tag> tags = tagService.getAll(limit, offset);

        for (Tag tag : tags) {
            tag.add(linkTo(methodOn(TagController.class).getTagById(tag.getId())).withSelfRel());
        }

        CollectionModel<Tag> result = CollectionModel.of(tags);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Returns {@link Tag} from a database by id or throws {@link ResourceExistenceException} if {@link Tag} not exist
     *
     * @param id - id's {@link Tag}
     * @return {@link ResponseEntity} with a {@link HttpStatus} and a {@link Tag} object.
     */
    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getTagById(@PathVariable Integer id) {
        Tag tag = tagService.getById(id);
        tag.add(linkTo(methodOn(TagController.class).getTagById(id)).withSelfRel());
        return new ResponseEntity<>(tag, HttpStatus.OK);
    }

    /**
     * Creates {@link Tag}
     *
     * @param tagDTO - object that will be converted to {@link TagDTO} and save to database
     * @return {@link ResponseEntity} with {@link HttpStatus} alone.
     */
    @PostMapping()
    public ResponseEntity<?> create(@RequestBody TagDTO tagDTO) {
        tagService.create(tagDTO);
        return new ResponseEntity<>(tagDTO, HttpStatus.CREATED);
    }

    /**
     * Deletes {@link Tag} by id if exist
     *
     * @param id {@link Tag} id which will be deleted
     * @return {@link ResponseEntity} with {@link HttpStatus} alone.
     */
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> deleteById(@PathVariable Integer id) {
        tagService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Returns the most widely used tag with highest cost of {@link com.epam.esm.entity.UserOrder}
     *
     * @return {@link ResponseEntity} with a {@link HttpStatus} and a most widely used {@link Tag} object or a {@link com.epam.esm.entity.ErrorData} object
     */
    @GetMapping(value = "/popular-tag")
    public ResponseEntity<?> getMostWidelyUsedTagWithHighestCost() {
        Tag tag = tagService.getMostWidelyUsedTagWithHighestCost();
        return new ResponseEntity<>(tag, HttpStatus.OK);
    }
}
