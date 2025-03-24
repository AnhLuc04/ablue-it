package com.ablueit.ecommerce.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "product_variations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductVariation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(nullable = false)
    private String size; // Size của sản phẩm

    @Column(nullable = false)
    private String color; // Màu sắc của sản phẩm

    @Column(nullable = false)
    private BigDecimal price; // Giá của biến thể

    private BigDecimal salePrice; // Giá khuyến mãi của biến thể

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product parentProduct; // Sản phẩm chính chứa biến thể này


}
