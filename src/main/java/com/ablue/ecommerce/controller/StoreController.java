package com.ablue.ecommerce.controller;
import com.ablue.ecommerce.service.StoreService;
import com.ablue.ecommerce.model.Store;
import com.ablue.ecommerce.model.User;
import com.ablue.ecommerce.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;

@Controller
public class StoreController {
    private final StoreService storeService;
    private final UserRepository userService;

    public StoreController(StoreService storeService, UserRepository userService) {
        this.storeService = storeService;
        this.userService = userService;
    }

    @GetMapping("/my-stores")
    public String getMyStores(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByUsername(auth.getName()).get();

        List<Store> stores = storeService.getStoresByUser(user);
        model.addAttribute("stores", stores);

        return "store/list"; // Giao diện hiển thị danh sách cửa hàng
    }
}
