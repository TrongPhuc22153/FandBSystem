package com.phucx.phucxfandb.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
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
    private BigDecimal unitPrice = BigDecimal.ZERO;

    @Column(name = "units_in_stock")
    private Integer unitsInStock = 0;

    @Column(name = "picture", length = 255)
    private String picture;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "is_featured")
    private Boolean isFeatured = false;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

    @OneToMany(mappedBy = "product")
    private List<Rating> ratings = new ArrayList<>();

    @OneToOne(mappedBy = "product")
    private ProductSize productSize;

    @ManyToMany
    @JoinTable(
            name = "products_discounts",
            inverseJoinColumns = {@JoinColumn(name = "discount_id", referencedColumnName = "discount_id")},
            joinColumns = {@JoinColumn(name = "product_id", referencedColumnName = "product_id")}
    )
    private List<Discount> discounts = new ArrayList<>();
}
