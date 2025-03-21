package com.ablueit.ecommerce.controller;

import com.ablueit.ecommerce.model.Product;
import com.ablueit.ecommerce.model.ProductVariation;
import com.ablueit.ecommerce.service.impl.ProductServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;

@Controller
@RequestMapping("/products/dashboard")
public class ProductDashboardController {

    @Autowired
    private ProductServiceImpl productService;

    // 🖥 Hiển thị form thêm sản phẩm
    @GetMapping("/add")
    public String showAddProductForm(Model model) {
        Product product = new Product();
        product.setVariations(new ArrayList<>());
        model.addAttribute("product", product);
        return "product-dashboard/create-product";
    }

    // ✅ Xử lý thêm sản phẩm
    @PostMapping("/add")
    public String addProduct(@ModelAttribute Product product) {
        productService.addProduct(product);
        return "redirect:/products/add?success";
    }
}
