package com.epam.esm.entity;

import com.epam.esm.audit.AuditType;
import com.epam.esm.util.AuditUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.apache.log4j.Logger;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "certificate")
public class GiftCertificate extends RepresentationModel<GiftCertificate> implements Identifiable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",columnDefinition = "INTEGER")
    private Integer id;
    @Column(name = "name",unique = true, nullable = false)
    private String name;
    @Column(name = "description")
    private String description;
    @Column(name = "price",columnDefinition = "BIGINT")
    private BigDecimal price;
    @Column(name = "duration")
    private Long duration;
    @Column(name = "create_date")
    private LocalDateTime createData;
    @Column(name = "last_update_date")
    private LocalDateTime lastUpdateDate;
    @ManyToMany(cascade =
            {CascadeType.PERSIST,
                    CascadeType.MERGE},
            fetch = FetchType.EAGER)
    @JoinTable(name = "gifts_and_tags",
            joinColumns = @JoinColumn(name = "certificate_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private List<Tag> tags;

    private static final String DATE_TIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS";

    @JsonFormat(pattern = DATE_TIME_PATTERN)
    public LocalDateTime getLastUpdateDate() {
        return lastUpdateDate;
    }

    @JsonProperty
    public List<Tag> getTags() {
        return tags;
    }

    @JsonFormat(pattern = DATE_TIME_PATTERN)
    public LocalDateTime getCreateData() {
        return createData;
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

