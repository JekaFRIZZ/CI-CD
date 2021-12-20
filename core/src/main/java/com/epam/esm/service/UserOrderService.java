package com.epam.esm.service;

import com.epam.esm.dao.GiftCertificateRepository;
import com.epam.esm.dao.UserRepository;
import com.epam.esm.dao.UserOrderRepository;
import com.epam.esm.dto.UserOrderDTO;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.User;
import com.epam.esm.entity.UserOrder;
import com.epam.esm.exception.ResourceExistenceException;
import com.epam.esm.util.DataUtils;
import com.epam.esm.validator.UserOrderValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserOrderService implements com.epam.esm.service.Service<UserOrder> {

    private final UserOrderRepository userOrderRepository;
    private final UserRepository userRepository;
    private final GiftCertificateRepository giftCertificateRepository;
    private final UserOrderValidator userOrderValidator;

    public UserOrderService(UserOrderRepository userOrderRepository, UserRepository userRepository, GiftCertificateRepository giftCertificateRepository, UserOrderValidator userOrderValidator) {
        this.userOrderRepository = userOrderRepository;
        this.userRepository = userRepository;
        this.giftCertificateRepository = giftCertificateRepository;
        this.userOrderValidator = userOrderValidator;
    }

    @Autowired


    private static final String DATE_TIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS";

    @Transactional
    public Integer create(UserOrderDTO userOrderDTO) {
        userOrderValidator.validate(userOrderDTO);

        Integer userId = userOrderDTO.getUserId();
        Integer certificateId = userOrderDTO.getCertificateId();

        Optional<User> user = userRepository.getById(userId);

        if(!user.isPresent()) {
         throw new ResourceExistenceException("User with id = " + userId + " doesn't exist", 777);
        }

        Optional<GiftCertificate> giftCertificate = giftCertificateRepository.getById(userId);

        if(!giftCertificate.isPresent()) {
            throw new ResourceExistenceException("User with id = " + certificateId + " doesn't exist", 777);
        }

        UserOrder userOrder = toUserOrder(userOrderDTO);

        LocalDateTime currentTime = DataUtils.getCurrentTime(DATE_TIME_PATTERN);
        userOrder.setTimestampPurchase(currentTime);

        return userOrderRepository.create(userOrder);
    }

    private UserOrder toUserOrder(UserOrderDTO userOrderDTO) {
        UserOrder userOrder = new UserOrder();

        userOrder.setUserId(userOrderDTO.getUserId());
        userOrder.setCertificateId(userOrderDTO.getCertificateId());
        userOrder.setPrice(userOrderDTO.getPrice());

        return userOrder;
    }

    public UserOrder getById(Integer id) {
        Optional<UserOrder> userOrder = userOrderRepository.getById(id);

        return userOrder.orElseThrow(
                () -> new ResourceExistenceException("User`s order with id = '" + id + "' doesn't exist", 777));
    }

    public List<UserOrder> getAll(int limit, int offset) {
        validatePaginationParams(limit, offset);
        return userOrderRepository.getAll(limit, offset);
    }

    public List<UserOrder> getAllByUserId(Integer userId, int limit, int offset) {
        validatePaginationParams(limit, offset);
        return userOrderRepository.getAllByUserId(userId, limit, offset);
    }

    @Transactional
    public void deleteById(Integer id) {
        Optional<UserOrder> userOrderOptional = userOrderRepository.getById(id);

        UserOrder userOrder = userOrderOptional.orElseThrow(
                () -> new ResourceExistenceException("User`s order with id = '" + id + "' doesn't exist", 777));

        userOrderRepository.deleteById(userOrder);
    }
}
