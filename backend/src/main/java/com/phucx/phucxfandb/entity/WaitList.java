package com.phucx.phucxfandb.entity;

import com.phucx.phucxfandb.constant.WaitListStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "wait_lists")
@EqualsAndHashCode(callSuper = false)
public class WaitList extends Auditable{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private String id;

    @Column(name = "contact_name", length = 30, nullable = false)
    private String contactName;

    @Column(name = "phone", length = 24)
    private String phone;

    @Builder.Default
    @Column(name = "party_size", nullable = false)
    private Integer partySize = 1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "table_id")
    private ReservationTable table;

    @EqualsAndHashCode.Exclude
    @OneToOne(mappedBy = "waitList", fetch = FetchType.LAZY)
    private Order order;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private WaitListStatus status = WaitListStatus.WAITING;

    @Column(name = "notes", length = 255)
    private String notes;
}
