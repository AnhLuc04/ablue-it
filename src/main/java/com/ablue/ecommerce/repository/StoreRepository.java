package com.ablue.ecommerce.repository;

import com.ablue.ecommerce.model.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StoreRepository extends JpaRepository<Store, Long> {
    boolean existsByName(String name);
    @Query(value = "SELECT * FROM stores WHERE user_id = :userId", nativeQuery = true)
    List<Store> findStoresByUserId(@Param("userId") Long userId);
}
