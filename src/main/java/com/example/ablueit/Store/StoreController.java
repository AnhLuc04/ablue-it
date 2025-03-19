package com.example.ablueit.Store;
import com.example.ablueit.Service.StoreService;
import com.example.ablueit.model.Store;
import com.example.ablueit.model.User;
import com.example.ablueit.repository.UserRepository;
import com.example.ablueit.security.CustomUserDetailsService;
import com.example.ablueit.security.UserService;
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
