package com.ablueit.ecommerce.repository;

import com.ablueit.ecommerce.model.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {
}
