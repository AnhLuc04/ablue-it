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

    private String imageUrl; // URL ảnh sản phẩm

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product; // Liên kết đến sản phẩm chính (nếu có)

    @ManyToOne
    @JoinColumn(name = "variation_id")
    private ProductVariation variation; // Liên kết đến biến thể (nếu có)
}
