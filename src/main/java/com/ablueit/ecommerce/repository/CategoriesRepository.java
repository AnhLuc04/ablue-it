package com.ablueit.ecommerce.repository;

import com.ablueit.ecommerce.model.Categories;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoriesRepository extends JpaRepository<Categories,Long> {
    List<Categories> findByStoreId(Long storeId);
}
