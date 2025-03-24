//package com.ablueit.ecommerce.model;
//
//import jakarta.persistence.*;
//import lombok.*;
//import java.math.BigDecimal;
//import java.util.List;
//
//@Entity
//@Table(name = "product_variations")
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//public class ProductVariation {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//
//
//    @Column(nullable = false)
//    private BigDecimal price; // Giá của biến thể
//    @Column(nullable = false)
//    private BigDecimal salePrice; // Giá khuyến mãi của biến thể
//
//    private List<Attribute> attributes;
//
//    @ManyToOne
//    @JoinColumn(name = "product_id", nullable = false)
//    private Product parentProduct; // Sản phẩm chính chứa biến thể này
//
//
//}





package com.ablueit.ecommerce.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

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

    private String color;
    private String size;
    private BigDecimal price;
    private BigDecimal salePrice;
//    @ManyToMany
//    @JoinTable(
//            name = "variation_attributes",
//            joinColumns = @JoinColumn(name = "variation_id"),
//            inverseJoinColumns = @JoinColumn(name = "attribute_id")
//    )
//    private List<Attribute> attributes;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    @JsonBackReference
    private Product parentProduct;
}
