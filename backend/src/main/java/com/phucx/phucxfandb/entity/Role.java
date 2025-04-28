package com.phucx.phucxfandb.entity;

import com.phucx.phucxfandb.constant.RoleName;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Roles")
public class Role extends Auditable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id", nullable = false, updatable = false)
    private Long roleId;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "role_name", length = 20, nullable = false)
    private RoleName roleName;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;
}
