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
@RequestMapping("/products/")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductController {

    StoreRepository storeRepository;
    CategoriesRepository categoriesRepository;
    ProductService productService;

    @GetMapping("/create-variation-product/{id}")
    public String showCreateVariationProduct(Model model, @PathVariable(value = "id") Long storeId) {
        log.info("GET /create-variation-product/{}", storeId);

        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new ResourceNotFoundException("store not found"));

        List<String> categories = categoriesRepository.findByStore(store).stream().map(Categories::getName).toList();

        model.addAttribute("storeId", store.getId());

        model.addAttribute("categories", categories);

        return "product-dashboard/create-variation-product";
    }

    @PostMapping("/create-variation-product/")
    public ResponseEntity<?> createVariationProduct(@RequestBody ProductRequest request) {
        log.info("POST /create-variation-product={}", request.toString());


        return ResponseEntity.ok().body(productService.addVariationProduct(request));
    }

}