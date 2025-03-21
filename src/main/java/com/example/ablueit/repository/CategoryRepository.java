package com.example.ablueit.repository;

import com.example.ablueit.model.Category;
import com.example.ablueit.model.Role;
import com.example.ablueit.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
//    @Query("SELECT c FROM Category c JOIN c.store s WHERE s.id = :storeId AND s.user = :user")
//    List<Category> findByStoreIdAndUser(@Param("storeId") Long storeId, @Param("user") User user);

}
