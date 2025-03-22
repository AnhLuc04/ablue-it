package com.ablueit.ecommerce.controller;

import com.ablueit.ecommerce.model.Categories;
import com.ablueit.ecommerce.model.Product;
import com.ablueit.ecommerce.model.Store;
import com.ablueit.ecommerce.repository.CategoriesRepository;
import com.ablueit.ecommerce.repository.StoreRepository;
import com.ablueit.ecommerce.service.impl.ProductServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/products/dashboard")
public class ProductDashboardController {

    @Autowired
    private ProductServiceImpl productService;

    @Autowired
    private CategoriesRepository categoryRepository;

    @Autowired
    private StoreRepository storeRepository;


    @GetMapping("/add/{storeId}")
    public String showAddProductForm(@PathVariable("storeId") Long storeId, Model model) {
        Product product = new Product();
        product.setVariations(new ArrayList<>());

        // Lấy danh sách danh mục theo storeId
        Store store =storeRepository.findById(storeId).get();
        List<Categories> categories = categoryRepository.findByStoreId(storeId);
        model.addAttribute("idStore",store.getId());
        model.addAttribute("product", product);
        model.addAttribute("categories", categories);
        return "product-dashboard/create-product";
    }



    // ✅ Xử lý thêm sản phẩm
    @PostMapping("/add")
    public String addProduct(@ModelAttribute Product product) {
        productService.addProduct(product);
        return "redirect:/products/add?success";
    }
}
