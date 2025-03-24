package com.ablueit.ecommerce.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String sku; // Mã SKU sản phẩm chính

    @Column(nullable = false)
    private String name; // Tên sản phẩm

    @Column(columnDefinition = "TEXT")
    private String description; // Mô tả sản phẩm

    @Column(columnDefinition = "TEXT")
    private String shortDescription; // Mô tả sản phẩm

    @Column(nullable = false)
    private BigDecimal price; // Giá sản phẩm

    private BigDecimal salePrice; // Giá khuyến mãi

    private Boolean isVariable; // TRUE nếu sản phẩm có biến thể (size, màu sắc)

    private Integer stockQuantity; // Số lượng tồn kho tổng (chỉ áp dụng cho sản phẩm đơn)

    private Boolean isInStock; // Còn hàng hay không

    private String status; // Trạng thái sản phẩm: "published", "draft", "pending"

 //   private String taxClass; // Loại thuế

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category; // Sản phẩm thuộc danh mục nào

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductImage> images; // Danh sách ảnh sản phẩm

//    @ManyToMany
//    @JoinTable(
//            name = "product_tags",
//            joinColumns = @JoinColumn(name = "product_id"),
//            inverseJoinColumns = @JoinColumn(name = "tag_id")
//    )
//    private List<Tag> tags; // Thẻ sản phẩm (tags giống WooCommerce)

//    @ManyToOne
//    @JoinColumn(name = "brand_id")
//    private Brand brand; // Nhãn hiệu sản phẩm (tương tự WooCommerce)

    @ManyToOne
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;
    @OneToMany(mappedBy = "parentProduct", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductVariation> variations; // Danh sách các biến thể (size, màu)

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now(); // Ngày tạo

    private LocalDateTime updatedAt; // Ngày cập nhật
}
