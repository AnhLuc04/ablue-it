package com.ablueit.ecommerce.controller;

import com.ablueit.ecommerce.exception.ResourceNotFoundException;
import com.ablueit.ecommerce.model.Attribute;
import com.ablueit.ecommerce.model.Categories;
import com.ablueit.ecommerce.model.Product;
import com.ablueit.ecommerce.model.Store;
import com.ablueit.ecommerce.payload.request.ProductRequest;
import com.ablueit.ecommerce.repository.AttributeRepository;
import com.ablueit.ecommerce.repository.CategoriesRepository;
import com.ablueit.ecommerce.repository.StoreRepository;
import com.ablueit.ecommerce.service.ProductService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j(topic = "PRODUCT-CONTROLLER")
@RequestMapping("/products/dashboard")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductController {

    StoreRepository storeRepository;
    CategoriesRepository categoriesRepository;
    AttributeRepository attributeRepository;
    ProductService productService;

    @GetMapping("/variant/add/{storeId}")
    public String showAddProductFormVariant(@PathVariable("storeId") Long storeId, Model model) {

        Product product = new Product();

        List<Attribute> attributes = attributeRepository.findAll();

        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new ResourceNotFoundException("store not found"));

        List<Categories> categories = categoriesRepository.findByStore(store);

        model.addAttribute("storeId", store.getId());
        model.addAttribute("product", product);
        model.addAttribute("categories", categories);
        model.addAttribute("attributes", attributes);
        return "product-dashboard/create-variant-product.html";
    }

    @PostMapping("/variations/add")
    ResponseEntity<String> addVariationProduct(@RequestBody ProductRequest request) {
        log.info("addVariationProduct={}", request.toString());

        return ResponseEntity.ok(productService.addVariationProduct(request));
    }


}