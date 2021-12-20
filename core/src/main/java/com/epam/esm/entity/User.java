package com.epam.esm.entity;

import com.epam.esm.audit.AuditType;
import com.epam.esm.util.AuditUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "user")
public class User extends RepresentationModel<User> implements Identifiable  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "username")
    private String username;
    @JsonIgnore
    @Column(name = "password")
    private String password;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "user_role",
                joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
                inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")})
    private List<Role> roles;

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
