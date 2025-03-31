package com.ablueit.ecommerce.repository.demo;

import com.ablueit.ecommerce.model.demo.AttributeTermEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttributeTermEntityRepository extends JpaRepository<AttributeTermEntity, Long> {}
