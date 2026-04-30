package com.breno.marketplace_test.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "product_images")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String url; // O link da foto que ficará guardada na nuvem ou servidor

    // MUITAS imagens pertencem a UM produto
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
}
