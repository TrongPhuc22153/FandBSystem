package com.phucx.phucxfandb.entity;

import java.time.LocalDate;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "employees")
@EqualsAndHashCode(callSuper = false)
public class Employee extends Auditable{
    @Id
    @Column(name = "employee_id", length = 36, updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private String employeeId;

    @Column(name = "title", length = 30)
    private String title;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "hire_date")
    private LocalDate hireDate;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @OneToOne
    @JoinColumn(name = "profile_id", referencedColumnName = "profile_id", nullable = false)
    private UserProfile profile;

    @Builder.Default
    @Column(name = "is_deleted")
    private Boolean isDeleted = false;
}
