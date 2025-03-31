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
@Table(name = "variation_entity")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VariationEntity {

  @Id
  @Column(name = "variation_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long id;

  @Column(name = "description")
  String description;

  @Column(name = "permalink")
  String permalink;

  @Column(name = "sku")
  String sku;

  @Column(name = "price")
  String price;

  @Column(name = "regular_price")
  String regularPrice;

  @Column(name = "sale_price")
  String salePrice;

  @Column(name = "date_start_sale")
  LocalDateTime dateStartSale;

  @Column(name = "date_end_sale")
  LocalDateTime dateEndSale;

  @Column(name = "is_on_sale")
  Boolean isOnSale;

  @Column(name = "status")
  ProductStatus status = ProductStatus.PUBLISHED;

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

  @ManyToOne
  @JsonIgnore
  @JoinColumn(name = "product_id")
  ProductEntity product;

  @ToString.Exclude
  @JsonIgnore
  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(
      name = "variation_attribute",
      joinColumns = @JoinColumn(name = "variation_id"),
      inverseJoinColumns = @JoinColumn(name = "attribute_id"))
  Set<AttributeEntity> attributes = new HashSet<>();

  @ToString.Exclude
  @JsonIgnore
  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(
      name = "variation_attribute_term",
      joinColumns = @JoinColumn(name = "variation_id"),
      inverseJoinColumns = @JoinColumn(name = "attribute_term_id"))
  Set<AttributeTermEntity> attributeTerms = new HashSet<>();
}
