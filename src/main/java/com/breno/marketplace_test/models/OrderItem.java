package com.breno.marketplace_test.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "ordered_items")
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
public class OrderItem extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    @ToString.Exclude
    private Order order;

    @Column(nullable = false)
    @Positive
    private Integer quantity;

    @Positive
    @Column(precision = 10, scale = 2)
    private BigDecimal pricePurchased;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

}
