package com.ablueit.ecommerce.service.impl;

import com.ablueit.ecommerce.model.Category;
import com.ablueit.ecommerce.model.Store;
import com.ablueit.ecommerce.model.User;
import com.ablueit.ecommerce.payload.request.CategoryRequest;
import com.ablueit.ecommerce.repository.CategoriesRepository;
import com.ablueit.ecommerce.repository.UserRepository;
import com.ablueit.ecommerce.service.CategoryService;
import com.ablueit.ecommerce.service.StoreService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "CATEGORY-SERVICE")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryServiceImpl implements CategoryService {

    CategoriesRepository categoriesRepository;
    StoreService storeService;
    UserRepository userRepository;

    @Override
    public String create(CategoryRequest request, RedirectAttributes redirectAttributes) {
        log.info("create={}", request.toString());

        if(categoriesRepository.existsByName(request.name())){
            log.error("category name existed={}", request.name());
            redirectAttributes.addFlashAttribute("errorMessageCategory", "Category name existed");
            return "redirect:/store/dashboard/" + request.storeId();
        }

        // get user for audit
        User seller = getUserFromAuthenticated();

        Store store = storeService.getStoryById(request.storeId());

        Category category = Category.builder()
                .name(request.name())
                .store(store)
                .createdBy(seller)
                .build();

        categoriesRepository.save(category);

        redirectAttributes.addFlashAttribute("successMessageCategory", "Created successfully");

        return "redirect:/store/dashboard/" + request.storeId();
    }

    @Override
    public List<Category> getCategoriesByStore(Store store) {
        log.info("getCategoriesByStore={}", store.getName());

        return categoriesRepository.findByStore(store);
    }

    private User getUserFromAuthenticated(){
        log.info("getUserFromAuthenticated");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName(); // Lấy username
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("user not found"));
    }
}
