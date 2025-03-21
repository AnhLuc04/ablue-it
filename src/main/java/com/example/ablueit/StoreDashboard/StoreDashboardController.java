package com.example.ablueit.StoreDashboard;

import com.example.ablueit.Service.StoreService;
import com.example.ablueit.model.Category;
import com.example.ablueit.model.Store;
import com.example.ablueit.model.User;
import com.example.ablueit.repository.CategoryRepository;
import com.example.ablueit.repository.StoreRepository;
import com.example.ablueit.repository.UserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

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
import java.util.Optional;


@Controller
@RequiredArgsConstructor
@RequestMapping("/store/dashboard")
@PreAuthorize("hasAnyRole('ROLE_SELLER')")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StoreDashboardController {

    StoreRepository storeService;
    UserRepository userRepository;
    CategoryRepository categoryRepository;


    @GetMapping("/{id}")
    public ModelAndView showDetailDashboard(@PathVariable("id") Long id) {
        ModelAndView modelAndView = new ModelAndView("store-dashboard/dashboard-store");

        // 🔥 Lấy thông tin tài khoản đang đăng nhập
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName(); // Lấy username
        Optional<User> userOptional = userRepository.findByUsername(username);

        if (userOptional.isEmpty()) {
            modelAndView.setViewName("error/403"); // Không có quyền truy cập
            return modelAndView;
        }

        User user = userOptional.get();

        // 🔥 Kiểm tra Store có thuộc về tài khoản này không?
//        Optional<Store> storeOptional = storeService.findByIdAndUser(id, user);
//        if (storeOptional.isEmpty()) {
//            modelAndView.setViewName("error/403"); // Không có quyền truy cập
//            return modelAndView;
//        }
//
//        Store store = storeOptional.get();

        // 🔥 Lấy danh sách danh mục chỉ của Store mà User này sở hữu
      //  List<Category> categories = categoryRepository.findByStoreIdAndUser(id, user);
//        modelAndView.addObject("store", store);
      //  modelAndView.addObject("categories", categories);
        return modelAndView;
    }


    @GetMapping("/detail/{id}")
    public ModelAndView showDetail(@PathVariable("id") Long id) {
        ModelAndView modelAndView = new ModelAndView("store-dashboard/detail-store");

        Store store = storeService.findById(id).orElseGet(null);
        if (store == null) {
            modelAndView.setViewName("error/404"); // Hiển thị trang lỗi nếu không tìm thấy
            return modelAndView;
        }

        modelAndView.addObject("store", store);
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

//        store.setDateTime(LocalDateTime.now());

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
