package com.epam.esm.controller;

import com.epam.esm.dto.UserOrderDTO;
import com.epam.esm.entity.User;
import com.epam.esm.entity.UserOrder;
import com.epam.esm.service.UserOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.util.List;

@RestController
@RequestMapping("/userOrders")
public class UserOrderController {

    private final UserOrderService userOrderService;

    @Autowired
    public UserOrderController(UserOrderService userOrderService) {
        this.userOrderService = userOrderService;
    }

    /**
     * Returns all {@link UserOrder} or all {@link User}'s {@link UserOrder} .
     *
     * @param userId the {@link User} id whose {@link UserOrder} objects will be searched for
     * @param limit a number of {@link UserOrder} objects to return
     * @param offset a number of {@link UserOrder} objects to skip when returning
     * @return {@link ResponseEntity} with a {@link HttpStatus} and a {@link List} of {@link UserOrder} objects
     */
    @GetMapping
    public ResponseEntity<?> getAll(@RequestParam(required = false) Integer userId,
                                    @RequestParam int limit,
                                    @RequestParam int offset) {

        List<UserOrder> userOrders;
        if (userId == null) {
            userOrders = userOrderService.getAll(limit, offset);
        } else {
            userOrders = userOrderService.getAllByUserId(userId, limit, offset);
        }

        for (UserOrder userOrder : userOrders) {
            userOrder.add(linkTo(methodOn(UserOrderController.class).getById(userOrder.getId())).withSelfRel());
        }

        return new ResponseEntity<>(userOrders, HttpStatus.OK);
    }

    /**
     *Returns {@link UserOrder} object
     *
     * @param id the {@link UserOrder} object's id that is to be retrieved from a database
     * @return {@link ResponseEntity} with a {@link HttpStatus} and a {@link UserOrder} object or a {@link com.epam.esm.entity.ErrorData} object
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Integer id) {
        UserOrder userOrder = userOrderService.getById(id);
        userOrder.add(linkTo(methodOn(UserOrderController.class).getById(id)).withSelfRel());
        return new ResponseEntity<>(userOrder, HttpStatus.OK);
    }

    /**
     *Creates {@link UserOrder} object or throws {@link com.epam.esm.exception.FieldExistenceException} if some fields
     * required for creation are missing.
     *
     * @param userOrderDTO dto object which will be mapped to {@link UserOrder}
     * @return {@link ResponseEntity} with a {@link HttpStatus} alone or additionally with a {@link com.epam.esm.entity.ErrorData} object
     */
    @PostMapping
    public ResponseEntity<?> create(@RequestBody UserOrderDTO userOrderDTO) {
        userOrderService.create(userOrderDTO);
        return new ResponseEntity<>(userOrderDTO, HttpStatus.CREATED);
    }

    /**
     * Deletes {@link UserOrder} by its id or throws {@link com.epam.esm.exception.ResourceExistenceException} if the object
     * with such id doesn't exist
     *
     * @param id`s object {@link UserOrder} which will be deleted
     * @return @link ResponseEntity} with a {@link HttpStatus} alone or additionally with a {@link com.epam.esm.entity.ErrorData} object.
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> deleteById(@PathVariable Integer id) {
        userOrderService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
