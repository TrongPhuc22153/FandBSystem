package com.phucx.phucxfandb.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "product_sizes")
@EqualsAndHashCode(callSuper = false)
public class ProductSize extends Auditable{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", length = 36, nullable = false, updatable = false)
    private String id;

    @Column(name = "height", nullable = false)
    private Integer height;

    @Column(name = "length", nullable = false)
    private Integer length;

    @Column(name = "weight", nullable = false)
    private Integer weight;
    
    @Column(name = "width", nullable = false)
    private Integer width;

    @OneToOne
    @JoinColumn(name = "product_id", referencedColumnName = "product_id", nullable = false)
    private Product product;
}
