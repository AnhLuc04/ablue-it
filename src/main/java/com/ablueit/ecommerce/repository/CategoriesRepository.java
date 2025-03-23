package com.ablueit.ecommerce.repository;

import com.ablueit.ecommerce.model.Category;
import com.ablueit.ecommerce.model.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoriesRepository extends JpaRepository<Category, Long> {

    boolean existsByName(String name);

    List<Category> findByStore(Store store);
}
