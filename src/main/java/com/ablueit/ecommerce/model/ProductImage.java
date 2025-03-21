package com.ablueit.ecommerce.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "product_images")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String imageUrl;

    // Thuộc về sản phẩm nào
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    // Ai là người up ảnh (Designer)
    @ManyToOne
    @JoinColumn(name = "uploaded_by")
    private User uploadedBy;
}
