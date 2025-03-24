package com.ablueit.ecommerce.repository;

import com.ablueit.ecommerce.model.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductVariationSampleRepository extends JpaRepository<ProductVariationSample,Long> {
    List<ProductVariationSample> findByStore(Store store);
}
