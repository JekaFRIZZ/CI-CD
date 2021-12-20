package com.epam.esm.controller;

import com.epam.esm.dto.GiftCertificateDTO;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.OrderSort;
import com.epam.esm.exception.ResourceExistenceException;
import com.epam.esm.service.GiftCertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/gifts")
public class GiftCertificateController {

    private final GiftCertificateService giftCertificateService;

    @Autowired
    public GiftCertificateController(GiftCertificateService giftCertificateService) {
        this.giftCertificateService = giftCertificateService;
    }

    /**
     * Returns all {@link GiftCertificate} from database
     *
     * @return {@link ResponseEntity} with a {@link HttpStatus} and all {@link GiftCertificate}.
     */
    @GetMapping()
    public ResponseEntity<?> getAll(@RequestParam Map<String, String> requestParam,
                                    @RequestParam int limit,
                                    @RequestParam int offset) {
        String fieldName = null;
        OrderSort isASC = OrderSort.ASC;
        List<GiftCertificate> giftCertificates;

        if (requestParam.get("fieldName") != null || requestParam.get("FIELDNAME") != null) {
            fieldName = requestParam.get("fieldName") != null ?
                    requestParam.get("fieldName") : requestParam.get("FIELDNAME");

            if (requestParam.get("sort") != null || requestParam.get("SORT") != null) {
                try {
                    isASC = requestParam.get("sort") != null ?
                            OrderSort.valueOf(requestParam.get("sort")) : OrderSort.valueOf(requestParam.get("SORT"));
                } catch (IllegalArgumentException e) {
                    throw new IllegalArgumentException("Incorrect sort parameter");
                }
            }
            giftCertificates = giftCertificateService.sortByOrder(fieldName, isASC, limit, offset);

        } else {
            giftCertificates = giftCertificateService.getAll(limit, offset);
        }
        for (GiftCertificate giftCertificate : giftCertificates) {
            giftCertificate.add
                    (linkTo(methodOn(GiftCertificateController.class).
                            getGiftById(giftCertificate.getId())).
                            withSelfRel());
        }
        return new ResponseEntity<>(giftCertificates, HttpStatus.OK);
    }

    /**
     * Returns {@link GiftCertificate} from a database by id or throws {@link ResourceExistenceException} if {@link GiftCertificate} not exist
     *
     * @param id - id's {@link GiftCertificate}
     * @return {@link ResponseEntity} with a {@link HttpStatus} and a {@link GiftCertificate} object.
     */
    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getGiftById(@PathVariable("id") Integer id) {
        GiftCertificate giftCertificate = giftCertificateService.getById(id);
        giftCertificate.add(linkTo(methodOn(GiftCertificateController.class).getGiftById(id)).withSelfRel());
        return new ResponseEntity<>(giftCertificate, HttpStatus.OK);
    }

    /**
     * Updates {@link GiftCertificate} by id's {@link GiftCertificate}
     *
     * @param id              - id's {@link GiftCertificate} that should be updated
     * @param giftCertificate - object which will be updated
     * @return {@link ResponseEntity} with {@link HttpStatus} alone.
     */
    @PatchMapping(value = "/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> updateById(@PathVariable Integer id, @RequestBody GiftCertificate giftCertificate) {
        giftCertificateService.update(id, giftCertificate);
        return new ResponseEntity<>(giftCertificate, HttpStatus.OK);
    }

    /**
     * Creates {@link GiftCertificate}
     *
     * @param giftCertificateDTO - object that will be converted to {@link GiftCertificate} and save to database
     * @return {@link ResponseEntity} with {@link HttpStatus} alone.
     */
    @PostMapping()
    public ResponseEntity<?> create(@RequestBody GiftCertificateDTO giftCertificateDTO) {
        Integer giftId = giftCertificateService.create(giftCertificateDTO);
        GiftCertificate giftCertificate = giftCertificateService.getById(giftId);
        return new ResponseEntity<>(giftCertificate, HttpStatus.CREATED);
    }


    /**
     * Deletes {@link GiftCertificate} by id if exist
     *
     * @param id {@link GiftCertificate} id which will be deleted
     * @return {@link ResponseEntity} with {@link HttpStatus} alone.
     */
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> delete(@PathVariable("id") Integer id) {
        giftCertificateService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}