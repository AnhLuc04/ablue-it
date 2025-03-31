package com.ablueit.ecommerce.repository.demo;

import com.ablueit.ecommerce.model.demo.AttributeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttributeEntityRepository extends JpaRepository<AttributeEntity, Long> {}
