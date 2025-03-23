package com.ablueit.ecommerce.repository;

import com.ablueit.ecommerce.model.Category;
import com.ablueit.ecommerce.model.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoriesRepository extends JpaRepository<Category, Long> {

    boolean existsByName(String name);

    List<Category> findByStore(Store store);

    Optional<Category> findById(Long id);
}
