package com.ablue.ecommerce.controller;

import com.ablue.ecommerce.model.Role;
import com.ablue.ecommerce.model.Store;
import com.ablue.ecommerce.model.User;
import com.ablue.ecommerce.repository.RoleRepository;
import com.ablue.ecommerce.repository.StoreRepository;
import com.ablue.ecommerce.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder; // ✅ Import PasswordEncoder
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
@Slf4j(topic = "ADMIN-DASHBOARD-CONTROLLER")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminDashboardController {

    RoleRepository roleRepository;
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;

    @GetMapping("/dashboard")
    public String adminDashboard(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        log.info("in dashboard");
        System.out.println("👤 User: " + userDetails.getUsername());
        System.out.println("🔑 Roles: " + userDetails.getAuthorities());
        model.addAttribute("role", userDetails.getAuthorities());
        model.addAttribute("username", userDetails.getUsername());
        return "admin-dashboard/admin";
    }

    @GetMapping("/create-seller")
    public ModelAndView showCreateSellerForm() {
        ModelAndView modelAndView = new ModelAndView("admin-dashboard/create-seller");
        modelAndView.addObject("user", new User());
        return modelAndView;
    }

    @PostMapping("/create-seller")
    public ModelAndView createSeller(@ModelAttribute("user") User user) {
        ModelAndView modelAndView = new ModelAndView("admin-dashboard/create-seller");

        if (userRepository.existsByUsername(user.getUsername())) {
            modelAndView.addObject("errorMessage", "Tài khoản đã tồn tại!");
            return modelAndView;
        }

        // Tạo và gán secret_key

        String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        User createby = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Seller not found"));


        // Tạo mới Seller
        User seller = new User();
        seller.setUsername(user.getUsername());
        seller.setPassword(passwordEncoder.encode(user.getPassword()));  // Mã hóa mật khẩu
        seller.setCreatedBy(createby);

        // Nếu email không được cung cấp, gán email mặc định
        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            seller.setEmail(user.getUsername() + "@gmail.com");  // Gán email mặc định
        } else {
            seller.setEmail(user.getEmail());  // Gán email từ form
        }

        // Tạo Role SELLER và gán vào User
        Role sellerRole = roleRepository.findByName("ROLE_SELLER")
                .orElseGet(() -> {
                    Role newRole = new Role();
                    newRole.setName("ROLE_SELLER");
                    return roleRepository.save(newRole);
                });

        seller.getRoles().add(sellerRole);  // Gán Role SELLER cho User

        userRepository.save(seller);  // Lưu Seller vào cơ sở dữ liệu

        modelAndView.addObject("successMessage", "Tạo tài khoản SELLER thành công!");
        return modelAndView;
    }

    @GetMapping("/create-store")
    public String showCreateStoreForm(Model model) {
//        model.addAttribute("store", new Store());
        return "admin-dashboard/create-store";
    }


//    @PostMapping("/create-store")
//    public String createStore(@ModelAttribute("store") Store store, Model model) {
//        // Lấy thông tin người dùng đang đăng nhập
//        String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
//        User seller = userRepository.findByUsername(username)
//                .orElseThrow(() -> new RuntimeException("Seller not found"));
//
//        if (storeRepository.existsByName(store.getName())) {
//            model.addAttribute("errorMessage", "Seller này đã có cửa hàng!");
//            return "admin-dashboard/create-store";
//        }
//
//        // Gán ngày giờ hiện tại cho cửa hàng
//        store.setDate(LocalDateTime.now());  // Sử dụng LocalDateTime.now() để lấy ngày giờ hiện tại
//
//        // Gán Seller vào cửa hàng
//        store.setSeller(seller);
//
//        // Lưu cửa hàng vào database
//        storeRepository.save(store);
//        model.addAttribute("successMessage", "Cửa hàng đã được tạo thành công!");
//        return "admin-dashboard/create-store";
//    }
}
