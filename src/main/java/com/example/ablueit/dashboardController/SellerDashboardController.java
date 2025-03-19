package com.example.ablueit.dashboardController;


import com.example.ablueit.model.Role;
import com.example.ablueit.model.Store;
import com.example.ablueit.model.User;
import com.example.ablueit.repository.RoleRepository;
import com.example.ablueit.repository.StoreRepository;
import com.example.ablueit.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Controller
@PreAuthorize("hasRole('ROLE_SELLER')")
@RequestMapping("/seller")
public class SellerDashboardController {
    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; // ✅ Inject PasswordEncoder
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRepository userService; // Service để lấy danh sách nhân viên

    @Autowired
    private StoreRepository websiteService; // Service để lấy danh sách website

    @GetMapping("/dashboard")
    public String sellerDashboard(Model model) {
        List<User> sellers = userService.findUsersCreatedBySeller(); // Lấy danh sách nhân viên (role SELLER)
        String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        User seller = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Seller not found"));

        List<Store> websites = websiteService.findStoresByUserId(seller.getId()); // Lấy danh sách website

        model.addAttribute("role", "Seller");
        model.addAttribute("sellers", sellers);
        model.addAttribute("websites", websites);

        return "seller-dashboard/seller-dashboard";
    }

    @GetMapping("/create-store")
    public String showCreateStoreForm(Model model) {
        model.addAttribute("store", new Store());
        return "seller-dashboard/create-store";
    }

    @PostMapping("/create-store")
    public String createStore(@ModelAttribute("store") Store store, Model model) {
        // Lấy thông tin người dùng đang đăng nhập
        String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        User seller = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Seller not found"));

        if (storeRepository.existsByName(store.getName())) { ;
            model.addAttribute("errorMessage", "Seller này đã có cửa hàng!");
            return "seller-dashboard/create-store";
        }

        // Gán ngày giờ hiện tại cho cửa hàng
        store.setDate(LocalDateTime.now());
        // Gán Seller vào cửa hàng
        store.setSeller(seller);

        // Lưu cửa hàng vào database
        storeRepository.save(store);
        model.addAttribute("successMessage", "Cửa hàng đã được tạo thành công!");
        return "seller-dashboard/create-store";
    }
    @GetMapping("/create-user")
    public ModelAndView showCreateSellerForm() {
        ModelAndView modelAndView = new ModelAndView("seller-dashboard/create-seller");
        modelAndView.addObject("user", new User());
        return modelAndView;
    }

    @PostMapping("/create-user")
    public ModelAndView createSeller(@ModelAttribute("user") User user) {
        ModelAndView modelAndView = new ModelAndView("seller-dashboard/create-seller");

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
        String secretKey = UUID.randomUUID().toString();
        seller.setSecretKey(secretKey);
        seller.setCreatedBy(createby);

        // Nếu email không được cung cấp, gán email mặc định
        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            seller.setEmail(user.getUsername() + "@gmail.com");  // Gán email mặc định
        } else {
            seller.setEmail(user.getEmail());  // Gán email từ form
        }

        // Tạo Role SELLER và gán vào User
        Role sellerRole = roleRepository.findByName("ROLE_USER")
                .orElseGet(() -> {
                    Role newRole = new Role();
                    newRole.setName("ROLE_USER");
                    return roleRepository.save(newRole);
                });

        seller.getRoles().add(sellerRole);  // Gán Role SELLER cho User

        userRepository.save(seller);  // Lưu Seller vào cơ sở dữ liệu

        modelAndView.addObject("successMessage", "Tạo tài khoản SELLER thành công!");
        return modelAndView;
    }
}
