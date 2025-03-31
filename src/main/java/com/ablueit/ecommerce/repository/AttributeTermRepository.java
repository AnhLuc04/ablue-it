package com.ablueit.ecommerce.repository;

import com.ablueit.ecommerce.model.demo.AttributeTermEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttributeTermRepository extends JpaRepository<AttributeTermEntity, Long> {}
