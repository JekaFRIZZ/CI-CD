package com.epam.esm.dto;

import lombok.*;

import java.math.BigDecimal;

@Data
public class UserOrderDTO {
    private Integer id;
    private Integer userId;
    private Integer certificateId;
    private BigDecimal price;
}
