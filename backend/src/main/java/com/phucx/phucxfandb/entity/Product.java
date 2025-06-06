package com.phucx.phucxfandb.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "products")
@EqualsAndHashCode(callSuper = false)
public class Product extends Auditable{
    @Id
    @Column(name = "product_id", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    @Column(name = "product_name", length = 40, nullable = false)
    private String productName;

    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "category_id", nullable = false)
    private Category category;

    @Column(name = "unit_price", precision = 10, scale = 2, nullable = false)
    private BigDecimal unitPrice;

    @Builder.Default
    @Column(name = "units_in_stock")
    private Integer unitsInStock = 0;

    @Builder.Default
    @Column(name = "minimum_stock")
    private Integer minimumStock = 0;

    @Column(name = "picture")
    private String picture;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Builder.Default
    @Column(name = "is_featured")
    private Boolean isFeatured = false;

    @Builder.Default
    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

    @Builder.Default
    @OneToMany(mappedBy = "product")
    private List<Rating> ratings = new ArrayList<>();

    @OneToOne(mappedBy = "product")
    private ProductSize productSize;

    @Version
    private Long version;
}
