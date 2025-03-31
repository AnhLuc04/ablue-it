package com.ablueit.ecommerce.controller;

import com.ablueit.ecommerce.exception.ResourceNotFoundException;
import com.ablueit.ecommerce.model.*;
import com.ablueit.ecommerce.payload.request.ProductRequest;
import com.ablueit.ecommerce.payload.request.VariantRequest;
import com.ablueit.ecommerce.repository.*;
import com.ablueit.ecommerce.service.ProductService;
import java.util.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@Slf4j(topic = "PRODUCT-DASHBOARD-CONTROLLER")
@RequiredArgsConstructor
@RequestMapping("/products/dashboard")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductDashboardController {

  AttributeTermRepository attributeTermRepository;
  VariantRepository variantRepository;
  ProductRepository productRepository;
  ProductVariationRepository productVariationRepository;
  CategoriesRepository categoryRepository;
  StoreRepository storeRepository;
  AttributeRepository attributeRepository;
  ProductService productService;

    @GetMapping("/single/add/{storeId}")
    public String showAddProductForm(@PathVariable("storeId") Long storeId, Model model) {
        Product product = new Product();
        product.setVariations(new ArrayList<>());

        // Lấy danh sách danh mục theo storeId
        Store store = storeRepository.findById(storeId).orElseThrow(() -> new ResourceNotFoundException("store not found"));
        List<Categories> categories = categoryRepository.findByStore(store);
        model.addAttribute("idStore", store.getId());
        model.addAttribute("product", product);
        model.addAttribute("categories", categories);
        return "product-dashboard/create-single-product.html";
    }



    @PostMapping("/single/add")
    public String addProduct(
            @RequestParam("storeId") Long storeId,
            @RequestParam("files") List<MultipartFile> images,
            @RequestParam("name") String name,
            @RequestParam("sku") String sku,
            @RequestParam("description") String description,
            @RequestParam("shortDescription") String shortDescription,
            @RequestParam("price") Double price,
            @RequestParam(value = "salePrice", required = false) Double salePrice,
            @RequestParam("categories") Long categoryId,
            @RequestParam("status") String status
    ) {
        Store store = storeRepository.findById(storeId).orElse(null);
        if (store == null) {
            return "redirect:/products/add?error=store_not_found";
        }

        Categories category = categoryRepository.findById(categoryId).orElse(null);
        if (category == null) {
            return "redirect:/products/add?error=category_not_found";
        }

        Product product = new Product();
        product.setStore(store);
        product.setName(name);
        product.setSku(sku);
        product.setDescription(description);
        product.setShortDescription(shortDescription);
        product.setPrice(price);
        product.setSalePrice(salePrice);
        product.setStatus(status);
        product.setCategory(category);
        product.setIsVariable(false);
        productRepository.save(product);

        return "redirect:/products/add?success";
    }

    @GetMapping("/variant/add/{storeId}")
    public String showAddProductFormVariant(@PathVariable("storeId") Long storeId, Model model) {
        Product product = new Product();
        product.setVariations(new ArrayList<>());

        // Lấy danh sách danh mục theo storeId
        Store store = storeRepository.findById(storeId).orElseThrow(() -> new ResourceNotFoundException("store not found"));
        List<Categories> categories = categoryRepository.findByStore(store);
        model.addAttribute("idStore", store.getId());
        model.addAttribute("product", product);
        model.addAttribute("categories", categories);
        return "product-dashboard/create-variant-product.html";
    }

    @PostMapping("/variant/add")
    public ResponseEntity<String> addProduct(@RequestBody ProductRequest request) {
        Product product = new Product();
        product.setName(request.getName());
        product.setSku(request.getSku());
        product.setPrice(request.getPrice());
        product.setSalePrice(request.getSalePrice());
        product.setStatus(request.getStatus());
        productRepository.save(product);

        // Lưu variants vào database
        if (request.getVariants() != null) {
            for (VariantRequest variantData : request.getVariants()) {
                Variant variant = new Variant();
                variant.setProduct(product);
                variant.setType(variantData.getType());
                variant.setValue(variantData.getValue());
                variantRepository.save(variant);
            }
        }

        return ResponseEntity.ok("Thêm sản phẩm thành công!");
    }

  @GetMapping("/{id}")
  public String showSingleProduct(@PathVariable Long id, Model model) {
    log.info("GET /products/{}", id);

    return productService.showSingleProduct(id, model);
  }
}