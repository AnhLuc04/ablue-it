package com.ablueit.ecommerce.repository.demo;

import com.ablueit.ecommerce.model.demo.VariationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VariationEntityRepository extends JpaRepository<VariationEntity, Long> {}
