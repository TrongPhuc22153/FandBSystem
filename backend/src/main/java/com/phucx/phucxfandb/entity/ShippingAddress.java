package com.phucx.phucxfandb.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "shipping_addresses")
@EqualsAndHashCode(callSuper = false)
public class ShippingAddress extends Auditable{
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ship_name", length = 40)
    private String shipName;

    @Column(name = "ship_address", length = 100)
    private String shipAddress;

    @Column(name = "ship_city", length = 50)
    private String shipCity;

    @Column(name = "ship_district", length = 50)
    private String shipDistrict;

    @Column(name = "ship_ward", length = 50)
    private String shipWard;

    @Column(name = "phone", length = 24)
    private String phone;

    @Builder.Default
    @Column(name = "is_deleted")
    private Boolean isDeleted = false;

    @Builder.Default
    @Column(name = "is_default")
    private Boolean isDefault = false;

    @ManyToOne
    private Customer customer;

}
