package com.ablueit.ecommerce.model.demo;

import com.ablueit.ecommerce.enums.ProductStatus;
import com.ablueit.ecommerce.enums.StockStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@ToString
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "product_entity")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductEntity {

  @Id
  @Column(name = "product_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long id;

  @Column(name = "name", nullable = false)
  String name;

  @Column(name = "slug")
  String slug;

  @Column(name = "permalink")
  String permalink;

  @Column(name = "status")
  @Enumerated(EnumType.STRING)
  ProductStatus status = ProductStatus.PUBLISHED;

  @Column(name = "description")
  String description;

  @Column(name = "short_description")
  String shortDescription;

  @Column(name = "sku")
  String sku;

  @Column(name = "price")
  String price; // current product price

  @Column(name = "regular_price")
  String regularPrice; // product regular price

  @Column(name = "sale_price")
  String salePrice;

  @Column(name = "date_start_sale")
  LocalDateTime dateStartSale;

  @Column(name = "date_end_sale")
  LocalDateTime dateEndSale;

  @Column(name = "is_on_sale")
  Boolean isOnSale;

  @Column(name = "total_sale")
  Integer totalSale;

  @Column(name = "stock_quantity")
  Integer stockQuantity;

  @Column(name = "stock_status")
  StockStatus stockStatus = StockStatus.IN_STOCK;

  @Column(name = "back_order_allowed")
  Boolean backOrderAllowed;

  @Column(name = "weight")
  String weight;

  @Column(name = "average_rating")
  String averageRating;

  @Column(name = "rating_count")
  Integer ratingCount;

  @ToString.Exclude
  @JsonIgnore
  @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
  Set<VariationEntity> variations = new HashSet<>();

  @ToString.Exclude
  @JsonIgnore
  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(
      name = "product_attribute",
      joinColumns = @JoinColumn(name = "product_id"),
      inverseJoinColumns = @JoinColumn(name = "attribute_id"))
  Set<AttributeEntity> attributes = new HashSet<>();
}
