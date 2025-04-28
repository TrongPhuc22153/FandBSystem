package com.phucx.phucxfandb.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User extends Auditable{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "user_id", length = 36, nullable = false)
    private String userId;

    @Column(name = "username", length = 20, nullable = false)
    private String username;

    @Column(name = "password", length = 255, nullable = false)
    private String password;

    @Email
    @Column(name = "email", length = 255, nullable = false)
    private String email;

    @Column(name = "enabled", nullable = false)
    private Boolean enabled = false;

    @Column(name = "email_verified", nullable = false)
    private Boolean emailVerified = false;

    @Column(name = "first_name", length = 20, nullable = false)
    private String firstName;

    @Column(name = "last_name", length = 30, nullable = false)
    private String lastName;

    @ManyToMany
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private UserProfile profile;
}
