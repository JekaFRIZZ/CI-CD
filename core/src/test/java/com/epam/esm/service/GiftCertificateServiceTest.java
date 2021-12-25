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
    public void testShouldGetAllGiftCertificatesWhenCorrectPaginateParamApplied() {
        int limit = 3;
        int offset = 2;
        giftCertificate.setId(giftCertificateId);
        when(mockGiftCertificateRepository.getAll(limit, offset)).thenReturn(giftCertificates);

        List<GiftCertificate> actual = giftCertificateService.getAll(limit, offset);

        assertEquals(giftCertificates, actual);
    }

    @Test
    public void testShouldGetGiftCertificateById() {
        giftCertificate.setId(giftCertificateId);
        when(mockGiftCertificateRepository.getById(giftCertificateId)).thenReturn(optionalGiftCertificate);

        GiftCertificate actual = giftCertificateService.getById(giftCertificateId);

        assertEquals(giftCertificate, actual);
    }
    @Test
    public void testShouldThrowExceptionWhenNonExistentGiftApplied() {
        assertThrows(ResourceExistenceException.class, () -> {
            when(mockGiftCertificateRepository.getById(giftCertificateId)).thenReturn(Optional.empty());

            giftCertificateService.getById(giftCertificateId);
        });
    }

    @Test
    public void testShouldThrowExceptionWhenGiftNotFoundById() {
        assertThrows(ResourceExistenceException.class, () -> {
            when(mockGiftCertificateRepository.getById(giftCertificateId)).thenReturn(Optional.empty());

            giftCertificateService.getById(giftCertificateId);
        });
    }

    @Test
    public void testDeleteShouldWorkCorrect() {
        when(mockGiftCertificateRepository.getById(giftCertificateId)).thenReturn(optionalGiftCertificate);
        doNothing().when(mockGiftCertificateRepository).deleteById(giftCertificate);

        giftCertificateService.deleteById(giftCertificateId);
    }

    @Test
    public void testShouldThrowExceptionWhenDeleteNonExistentTag() {
        assertThrows(ResourceExistenceException.class, () -> {
            when(mockGiftCertificateRepository.getById(giftCertificateId)).thenReturn(Optional.empty());

            giftCertificateService.deleteById(giftCertificateId);
        });
    }
}