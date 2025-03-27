package com.ablueit.ecommerce.repository;

import com.ablueit.ecommerce.model.Attribute;
import com.ablueit.ecommerce.model.AttributeTerm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AttributeTermRepository extends JpaRepository<AttributeTerm,Long> {
    Optional<AttributeTerm> findByNameAndAttribute(String name, Attribute attribute);
}
