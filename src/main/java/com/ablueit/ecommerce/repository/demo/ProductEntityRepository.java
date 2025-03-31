package com.ablueit.ecommerce.repository.demo;

import com.ablueit.ecommerce.model.demo.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductEntityRepository extends JpaRepository<ProductEntity, Long> {}
