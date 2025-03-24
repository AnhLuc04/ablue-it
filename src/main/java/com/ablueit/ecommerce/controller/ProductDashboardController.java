package com.ablueit.ecommerce.controller;

import com.ablueit.ecommerce.exception.ResourceNotFoundException;
import com.ablueit.ecommerce.model.*;
import com.ablueit.ecommerce.repository.*;
import com.ablueit.ecommerce.service.impl.ProductServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/products/dashboard")
public class ProductDashboardController {

    @Autowired
    private ProductRepository productService;

    @Autowired
    private ProductVariationRepository productVariationRepository;

    @Autowired
    private CategoriesRepository categoryRepository;

    @Autowired
    private StoreRepository storeRepository;


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
            @RequestParam("price") BigDecimal price,
            @RequestParam(value = "salePrice", required = false) BigDecimal salePrice,
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
        productService.save(product);

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


    @PostMapping("variant/add")
    public String addProduct(
            @RequestParam("storeId") Long storeId,
            @RequestParam("files") List<MultipartFile> images,
            @RequestParam("name") String name,
            @RequestParam("sku") String sku,
            @RequestParam("description") String description,
            @RequestParam("shortDescription") String shortDescription,
            @RequestParam("price") BigDecimal price,
            @RequestParam(value = "salePrice", required = false) BigDecimal salePrice,
            @RequestParam("categories") Long categoryId,
            @RequestParam("isVariable") boolean isVariable,
            @RequestParam("status") String status,
            @RequestParam(name = "variationColors", required = false) List<String> variationColors,
            @RequestParam(name = "variationSizes", required = false) List<String> variationSizes,
            @RequestParam(name = "variationPrices", required = false) List<BigDecimal> variationPrices,
            @RequestParam(name = "variationSalePrices", required = false) List<BigDecimal> variationSalePrices,
            @RequestParam(name = "variationStocks", required = false) List<Integer> variationStocks
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
        product.setIsVariable(isVariable);
        product.setStatus(status);
        product.setCategory(category);

        product = productService.save(product);

        if (isVariable && variationColors != null && variationSizes != null) {
            List<ProductVariation> variations = new ArrayList<>();

            for (int i = 0; i < variationColors.size(); i++) {
                ProductVariation variation = new ProductVariation();
                variation.setParentProduct(product);
                variation.setColor(variationColors.get(i));
                variation.setSize(variationSizes.get(i));
                variation.setPrice(variationPrices.get(i));  // Giá riêng từng biến thể
                variation.setSalePrice(variationSalePrices.get(i));  // Giá KM từng biến thể
                variations.add(variation);
            }

            productVariationRepository.saveAll(variations);
            product.setVariations(variations);
        }

        productService.save(product);

        return "redirect:/products/add?success";
    }

}