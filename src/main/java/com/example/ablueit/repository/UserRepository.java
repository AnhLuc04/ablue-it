package com.example.ablueit.repository;

import com.example.ablueit.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
    // Tìm tất cả user do SELLER tạo
    @Query("SELECT u FROM User u WHERE u.createdBy IN " +
            "(SELECT seller FROM User seller JOIN seller.roles r WHERE r.name = 'ROLE_SELLER')")
    List<User> findUsersCreatedBySeller();
}
