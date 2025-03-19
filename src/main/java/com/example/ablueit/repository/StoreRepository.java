package com.example.ablueit.repository;

import com.example.ablueit.model.Store;
import com.example.ablueit.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StoreRepository extends JpaRepository<Store, Long> {
    boolean existsByName(String name);
    @Query(value = "SELECT * FROM stores WHERE user_id = :userId", nativeQuery = true)
    List<Store> findStoresByUserId(@Param("userId") Long userId);
}
