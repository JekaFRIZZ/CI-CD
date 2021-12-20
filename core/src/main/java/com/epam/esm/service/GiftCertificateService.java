package com.epam.esm.service;


import com.epam.esm.dao.GiftCertificateRepository;
import com.epam.esm.dto.GiftCertificateDTO;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.OrderSort;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.DuplicateResourceException;
import com.epam.esm.exception.FieldExistenceException;
import com.epam.esm.exception.ResourceExistenceException;
import com.epam.esm.util.DataUtils;
import com.epam.esm.validator.GiftCertificateValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

public class GiftCertificateService implements Service<GiftCertificate> {

    private final GiftCertificateRepository giftCertificateRepository;
    private final TagService tagService;
    private final GiftCertificateValidator giftCertificateValidator;

    private static final String DATE_TIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS";

    @Autowired
    public GiftCertificateService(GiftCertificateRepository giftCertificateRepository,
                                  TagService tagService,
                                  GiftCertificateValidator giftCertificateValidator) {
        this.giftCertificateRepository = giftCertificateRepository;
        this.tagService = tagService;
        this.giftCertificateValidator = giftCertificateValidator;
    }

    public List<GiftCertificate> getAll(int limit, int offset) {
        validatePaginationParams(limit, offset);
        return giftCertificateRepository.getAll(limit, offset);
    }

    public GiftCertificate getById(Integer id) {
        Optional<GiftCertificate> giftCertificateOptional = giftCertificateRepository.getById(id);

        if (!giftCertificateOptional.isPresent()) {
            throw new ResourceExistenceException("The gift certificate with such id = '" + id + "' doesn't exist", 7777);
        }

        GiftCertificate giftCertificate = giftCertificateOptional.get();

        return giftCertificate;
    }

    @Transactional
    public Integer create(GiftCertificateDTO giftCertificateDTO) {
        Optional<GiftCertificate> giftCertificateOptional = giftCertificateRepository.getByName(giftCertificateDTO.getName());
        if (giftCertificateOptional.isPresent()) {
            throw new DuplicateResourceException("The gift certificate with name '" +
                    giftCertificateDTO.getName() + "' already exist", 7777);
        }

        giftCertificateValidator.validate(giftCertificateDTO);

        GiftCertificate giftCertificate = toGiftCertificate(giftCertificateDTO);

        LocalDateTime currentTime = DataUtils.getCurrentTime(DATE_TIME_PATTERN);
        giftCertificate.setCreateData(currentTime);
        giftCertificate.setLastUpdateDate(currentTime);

        persistGiftCertificateTag(giftCertificate);

        return giftCertificateRepository.create(giftCertificate);
    }

    private void persistGiftCertificateTag(GiftCertificate giftCertificate) {
        List<Tag> tags = giftCertificate.getTags();
        List<Tag> persistedTags = tagService.getPersistedTags(tags);
        giftCertificate.setTags(persistedTags);
    }

    private GiftCertificate toGiftCertificate(GiftCertificateDTO giftCertificateDTO) {
        GiftCertificate giftCertificate = new GiftCertificate();

        giftCertificate.setName(giftCertificateDTO.getName());
        giftCertificate.setDescription(giftCertificateDTO.getDescription());
        giftCertificate.setPrice(giftCertificateDTO.getPrice());
        giftCertificate.setDuration(giftCertificateDTO.getDuration());
        giftCertificate.setCreateData(giftCertificateDTO.getCreateData());
        giftCertificate.setLastUpdateDate(giftCertificateDTO.getLastUpdateDate());
        giftCertificate.setTags(giftCertificateDTO.getTags());

        return giftCertificate;
    }

    private void createTagsForGift(List<Tag> tags) {
        tagService.updateTags(tags);
    }

    @Transactional
    public void update(Integer id, GiftCertificate giftCertificate) {
        Optional<GiftCertificate> giftCertificateOptional = giftCertificateRepository.getById(id);

        if (!giftCertificateOptional.isPresent()) {
            throw new ResourceExistenceException("The gift certificate with such id = '" + id + "' doesn't exist", 777);
        }
        giftCertificate.setLastUpdateDate(DataUtils.getCurrentTime(DATE_TIME_PATTERN));
        GiftCertificate update = makeUpdateGift(giftCertificateOptional.get(), giftCertificate);
        giftCertificateRepository.update(update);

        List<Tag> tags = giftCertificate.getTags();
        if (tags != null && !tags.isEmpty()) {
            createTagsForGift(tags);
        }
    }

    private GiftCertificate makeUpdateGift(GiftCertificate current, GiftCertificate update) {

        String name = update.getName();
        String description = update.getDescription();
        BigDecimal price = update.getPrice();
        Long duration = update.getDuration();
        LocalDateTime createDate = update.getCreateData();
        LocalDateTime lastUpdateDate = update.getLastUpdateDate();

        if (name != null) {
            current.setName(name);
        }
        if (description != null) {
            current.setDescription(description);
        }
        if (price != null) {
            current.setPrice(price);
        }
        if (duration != null) {
            current.setDuration(duration);
        }
        if (createDate != null) {
            current.setCreateData(createDate);
        }
        if (lastUpdateDate != null) {
            current.setLastUpdateDate(lastUpdateDate);
        }

        return current;
    }

    @Transactional
    public void deleteById(Integer id) {
        Optional<GiftCertificate> gift = giftCertificateRepository.getById(id);
        if (!gift.isPresent()) {
            throw new ResourceExistenceException("The gift certificate with such id = '" + id + "' doesn't exist", 7777);
        }
        giftCertificateRepository.deleteById(gift.get());
    }

    public List<GiftCertificate> sortByOrder(String fieldName, OrderSort isASC, int limit, int offset) {
        List<String> fieldNames =
                Arrays.asList("id", "name", "description", "price", "duration", "createData", "lastUpdateDate");
        if (!fieldNames.contains(fieldName)) {
            throw new FieldExistenceException("Field with name of '" + fieldName + "' doesn't exist", 77777);
        }
        List<GiftCertificate> giftCertificates = giftCertificateRepository.sortByOrder(fieldName, isASC, limit, offset);

        return giftCertificates;
    }
}
