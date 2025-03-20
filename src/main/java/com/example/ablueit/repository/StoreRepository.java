package com.example.ablueit.repository;

import com.example.ablueit.model.Store;
import com.example.ablueit.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StoreRepository extends JpaRepository<Store, Long> {

    @Query(value = "SELECT * FROM stores WHERE user_id = :userId", nativeQuery = true)
    List<Store> findStoresByUserId(@Param("userId") Long userId);
    @Query("SELECT s FROM Store s WHERE s.seller IN (SELECT u FROM User u JOIN u.roles r WHERE r.name = 'ROLE_SELLER' AND u.createdBy.username = :adminUsername)")
    List<Store> findStoresBySellersCreatedByAdmin(@Param("adminUsername") String adminUsername);
    boolean existsByName(String name);
    boolean existsByEmail(String email); // ✅ Thêm kiểm tra email
}
