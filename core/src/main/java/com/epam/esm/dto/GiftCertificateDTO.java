package com.epam.esm.dto;

import com.epam.esm.entity.Tag;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class GiftCertificateDTO {

    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Long duration;
    private LocalDateTime createData;
    private LocalDateTime lastUpdateDate;
    private List<Tag> tags;

}
