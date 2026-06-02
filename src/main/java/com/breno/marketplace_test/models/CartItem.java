package com.breno.marketplace_test.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "cart_items", uniqueConstraints = {
        @UniqueConstraint(name = "uk_cart_product", columnNames = {"cart_id", "product_id"})
})
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
public class CartItem extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cart_id", nullable = false)
    private ShoppingCart cart;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private Integer quantity;

}
