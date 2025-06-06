package com.phucx.phucxfandb.entity;

import com.phucx.phucxfandb.enums.RoleName;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Roles")
@EqualsAndHashCode(callSuper = false)
public class Role extends Auditable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id", nullable = false, updatable = false)
    private Long roleId;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "role_name", length = 20, nullable = false)
    private RoleName roleName;

    @Builder.Default
    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;
}
