package com.epam.esm.service;

import com.epam.esm.dao.GiftCertificateRepository;
import com.epam.esm.dto.GiftCertificateDTO;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.ResourceExistenceException;
import com.epam.esm.validator.GiftCertificateValidator;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GiftCertificateServiceTest {
    private final GiftCertificateRepository mockGiftCertificateRepository = mock(GiftCertificateRepository.class);
    private final TagService mockTagService = mock(TagService.class);
    private final GiftCertificateValidator mockGiftCertificateValidator = mock(GiftCertificateValidator.class);
    private final GiftCertificateService giftCertificateService = new GiftCertificateService(
            mockGiftCertificateRepository,
            mockTagService,
            mockGiftCertificateValidator
    );

    private final List<GiftCertificate> giftCertificates = new ArrayList<>();
    private final List<Tag> tags = new ArrayList<>();
    private final GiftCertificate giftCertificate = new GiftCertificate();
    private final GiftCertificateDTO giftCertificateDTO = new GiftCertificateDTO();
    private final Integer giftCertificateId = 1;
    private final Optional<GiftCertificate> optionalGiftCertificate = Optional.of(giftCertificate);
    private final Map<String, String> map = new HashMap<>();

    @Test
    void testShouldGetAllGiftCertificatesWhenCorrectPaginateParamApplied() {
        int limit = 3;
        int offset = 2;
        giftCertificate.setId(giftCertificateId);
        when(mockGiftCertificateRepository.getAll(limit, offset)).thenReturn(giftCertificates);

        List<GiftCertificate> actual = giftCertificateService.getAll(limit, offset);

        assertEquals(giftCertificates, actual);
    }

    @Test
    void testShouldGetGiftCertificateById() {
        giftCertificate.setId(giftCertificateId);
        when(mockGiftCertificateRepository.getById(giftCertificateId)).thenReturn(optionalGiftCertificate);

        GiftCertificate actual = giftCertificateService.getById(giftCertificateId);

        assertEquals(giftCertificate, actual);
    }

    @Test
    void testShouldThrowExceptionWhenNonExistentGiftApplied() {
        when(mockGiftCertificateRepository.getById(giftCertificateId)).thenReturn(Optional.empty());

        assertThrows(ResourceExistenceException.class, () -> {
            giftCertificateService.getById(giftCertificateId);
        });
    }

    @Test
    void testShouldThrowExceptionWhenGiftNotFoundById() {
        when(mockGiftCertificateRepository.getById(giftCertificateId)).thenReturn(Optional.empty());

        assertThrows(ResourceExistenceException.class, () -> {
            giftCertificateService.getById(giftCertificateId);
        });
    }

    @Test
    void testShouldThrowExceptionWhenDeleteNonExistentTag() {
        when(mockGiftCertificateRepository.getById(giftCertificateId)).thenReturn(Optional.empty());

        assertThrows(ResourceExistenceException.class, () -> {
            giftCertificateService.deleteById(giftCertificateId);
        });
    }
}