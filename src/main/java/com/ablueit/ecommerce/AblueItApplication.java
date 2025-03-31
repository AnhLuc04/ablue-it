package com.ablueit.ecommerce;

import com.ablueit.ecommerce.model.*;
import com.ablueit.ecommerce.repository.ProductRepository;
import com.ablueit.ecommerce.repository.RoleRepository;
import com.ablueit.ecommerce.repository.UserRepository;
import java.util.Collections;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class AblueItApplication {

    public static void main(String[] args) {
        SpringApplication.run(AblueItApplication.class, args);
    }

  @Bean
  CommandLineRunner init(
      UserRepository userRepository,
      RoleRepository roleRepository,
      PasswordEncoder passwordEncoder,
      ProductRepository productRepository) {
    return args -> {
      // Kiểm tra xem ROLE_ADMIN đã tồn tại chưa
      Role adminRole =
          roleRepository
              .findByName("ROLE_ADMIN")
              .orElseGet(
                  () -> {
                    Role newRole = new Role();
                    newRole.setName("ROLE_ADMIN");
                    return roleRepository.save(newRole); // Lưu Role vào DB trước
                  });

      // Kiểm tra xem user "admin" đã tồn tại chưa
      if (userRepository.findByUsername("admin").isEmpty()) {
        User admin = new User();
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setEmail("admin@example.com");
        admin.setRoles(Collections.singleton(adminRole)); // Gán role đã lưu);
        userRepository.save(admin);
        System.out.println("✅ Tài khoản ADMIN đã được tạo thành công!");
      } else {
        System.out.println("⚠️ Tài khoản ADMIN đã tồn tại, không cần tạo lại.");
      }

      //        User user = userRepository.findById(2L).orElseThrow(RuntimeException::new);
      //
      //        Store store = user.getStore();
      //
      //        Product product = Product.builder()
      //                .name("ABC")
      //                .sku("concac")
      //                .description("ABC")
      //                .images(null)
      //                .isInStock(true)
      //                .isVariable(false)
      //                .price(10.2)
      //                .salePrice(10.2)
      //                .shortDescription("ABC")
      //                .store(store)
      //                .status("ACTIVE")
      //                .build();
      //
      //        productRepository.save(product);

    };
}


}
