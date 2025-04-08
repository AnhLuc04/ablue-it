package com.ablueit.ecommerce.repository;

import com.ablueit.ecommerce.model.Variation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductVariationRepository extends JpaRepository<Variation, Long> {
}
