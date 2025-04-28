package com.phucx.phucxfandb.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user_profiles")
public class UserProfile extends Auditable{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "profile_id", nullable = false, length = 36, updatable = false)
    private String profileId;

    @Column(name = "address", length = 100)
    private String address;

    @Column(name = "city", length = 50)
    private String city;

    @Column(name = "district", length = 50)
    private String district;

    @Column(name = "ward", length = 50)
    private String ward;

    @Column(name = "phone", length = 24)
    private String phone;

    @Column(name = "picture", length = 255)
    private String picture;

    @OneToOne(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true)
    private Customer customer;

    @OneToOne(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true)
    private Employee employee;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
    private User user;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;
}
