package com.ablueit.ecommerce.repository;

import com.ablueit.ecommerce.model.Product;
import com.ablueit.ecommerce.payload.request.VariantRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    boolean existsBySku(String sku);
}