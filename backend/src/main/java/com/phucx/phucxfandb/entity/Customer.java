package com.phucx.phucxfandb.entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "customers")
@EqualsAndHashCode(callSuper = false)
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

    @Builder.Default
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "customer")
    private List<ShippingAddress> shippingAddresses = new ArrayList<>();

    @Builder.Default
    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;
}
