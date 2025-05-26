package com.ablueit.ecommerce.repository;

import com.ablueit.ecommerce.model.Categories;
import com.ablueit.ecommerce.model.Product;
import com.ablueit.ecommerce.model.Store;
import com.ablueit.ecommerce.payload.request.VariantRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    boolean existsBySku(String sku);

    @Query(value = "SELECT product_id from product_category where category_id = :categoryId", nativeQuery = true)
    List<Long> findAllByCategories(@Param("categoryId") Long categoryId);

    List<Product> findAllByStore(Store store);

}