package com.epam.esm.entity;

import com.epam.esm.audit.AuditType;
import com.epam.esm.util.AuditUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.apache.log4j.Logger;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "user_order")
public class UserOrder extends RepresentationModel<UserOrder> implements Identifiable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "user_id")
    private Integer userId;
    @Column(name = "certificate_id")
    private Integer certificateId;
    @Column(name = "price",columnDefinition = "BIGINT")
    private BigDecimal price;
    @Column(name = "timestamp_purchase", columnDefinition = "VARCHAR(30)")
    private LocalDateTime timestampPurchase;

    private static final String DATE_TIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS";

    @JsonFormat(pattern = DATE_TIME_PATTERN)
    public LocalDateTime getTimestampPurchase() {
        return timestampPurchase;
    }

    @PostPersist
    public void onPostPersist() {
        AuditUtils.audit(AuditType.INSERT, this);
    }

    @PostUpdate
    public void onPostUpdate() {
        AuditUtils.audit(AuditType.UPDATE, this);
    }

    @PostRemove
    public void onPostRemove() {
        AuditUtils.audit(AuditType.REMOVE, this);
    }

}
