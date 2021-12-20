package com.epam.esm.entity;

import com.epam.esm.audit.AuditType;
import com.epam.esm.util.AuditUtils;
import lombok.*;
import org.apache.log4j.Logger;
import org.hibernate.envers.Audited;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tag")
@Audited
public class Tag extends RepresentationModel<Tag> implements Identifiable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "name")
    private String name;

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
