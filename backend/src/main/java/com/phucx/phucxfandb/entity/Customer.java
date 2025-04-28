package com.phucx.phucxfandb.entity;


import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "customers")
public class Customer extends Auditable{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "customer_id", length = 36, nullable = false, updatable = false)
    private String customerId;

    @Column(name = "contact_name", length = 30, nullable = false)
    private String contactName;

    @OneToOne
    @JoinColumn(name = "profile_id", referencedColumnName = "profile_id", nullable = false)
    private UserProfile profile;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;
}
