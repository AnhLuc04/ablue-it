package com.example.ablueit.StoreDashboard;

import com.example.ablueit.Service.StoreService;
import com.example.ablueit.model.Store;
import com.example.ablueit.model.User;
import com.example.ablueit.repository.StoreRepository;
import com.example.ablueit.repository.UserRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SELLER')")
@RequestMapping("/store/dashboard")
public class StoreDashboardController {
    private final StoreRepository storeService;
    private final UserRepository userRepository;

    public StoreDashboardController(StoreRepository storeService, UserRepository userRepository) {
        this.storeService = storeService;
        this.userRepository = userRepository;
    }

    @GetMapping("/detail/{id}")
    public ModelAndView showDetail(@PathVariable("id") Long id) {
        ModelAndView modelAndView = new ModelAndView("store-dashboard/detail-store");

        Store store = storeService.findById(id).orElseThrow(); // Đảm bảo có phương thức getStoreById(id)
        if (store == null) {
            modelAndView.setViewName("error"); // Chuyển hướng đến trang lỗi nếu store không tồn tại
        } else {
            modelAndView.addObject("store", store);
        }

        return modelAndView;
    }


    @GetMapping("/create-store")
    public String showCreateStoreForm(Model model) {
        model.addAttribute("store", new Store());
        return "store-dashboard/create-store";
    }

    @PostMapping("/create-store")
    public String createStore(@ModelAttribute("store") Store store, Model model) {
        String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        User seller = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Seller not found"));

        // Kiểm tra nếu cửa hàng đã tồn tại
        if (storeService.existsByName(store.getName()) || storeService.existsByEmail(store.getEmail())) {
            model.addAttribute("errorMessage", "Tên cửa hàng hoặc email đã tồn tại!");
            return "store-dashboard/create-store";
        }

        store.setDateTime(LocalDateTime.now());
        store.setSeller(seller);

        storeService.save(store);
        model.addAttribute("successMessage", "Cửa hàng đã được tạo thành công!");
        return "store-dashboard/create-store";
    }


    // 4️⃣ Hiển thị form chỉnh sửa Store
    @GetMapping("/edit/{id}")
    public String showEditStoreForm(@PathVariable Long id, Model model) {
        Store store = storeService.findById(id)
                .orElseThrow(() -> new RuntimeException("Store not found"));
        model.addAttribute("store", store);
        return "store-dashboard/store-form";
    }

    // 5️⃣ Cập nhật Store (Admin hoặc chủ sở hữu)
    @PostMapping("/update/{id}")
    public String updateStore(@PathVariable Long id, @ModelAttribute Store updatedStore) {
        Store existingStore = storeService.findById(id)
                .orElseThrow(() -> new RuntimeException("Store not found"));
        checkPermission(existingStore);

        existingStore.setName(updatedStore.getName());
        existingStore.setAddress(updatedStore.getAddress());
        storeService.save(existingStore);

        return "redirect:/store/dashboard";
    }

    // 6️⃣ Xóa Store
    @GetMapping("/delete/{id}")
    public String deleteStore(@PathVariable Long id) {
        storeService.deleteById(id);
        return "redirect:/store/dashboard";
    }
    private void checkPermission(Store store) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Chuyển đổi danh sách roles thành chuỗi và kiểm tra quyền
        boolean isAdmin = user.getRoles().stream().anyMatch(role -> role.getName().equals("ROLE_ADMIN"));
        boolean isOwner = store.getSeller().equals(user);

        if (!isAdmin && !isOwner) {
            throw new RuntimeException("Unauthorized to modify this store");
        }
    }

}
