package com.ablueit.ecommerce.controller;

import com.ablueit.ecommerce.exception.ResourceNotFoundException;
import com.ablueit.ecommerce.model.*;
import com.ablueit.ecommerce.payload.request.ProductRequest;
import com.ablueit.ecommerce.payload.request.VariantRequest;
import com.ablueit.ecommerce.repository.*;
import com.ablueit.ecommerce.service.impl.ProductServiceImpl;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

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
    @Autowired
    private AttributeRepository attributeRepository;
    @Autowired
    private AttributeTermRepository attributeTermRepository;


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






    @Transactional
    @PostMapping("/variant/add")
    public String addProduct(@ModelAttribute ProductRequest productRequest,
                             @RequestParam(value = "files", required = false) MultipartFile[] files) {
        try {
            // Xử lý file upload (nếu có)
            if (files != null) {
                for (MultipartFile file : files) {
                    if (!file.isEmpty()) {
                        // TODO: Xử lý file, lưu vào server hoặc cloud
                    }
                }
            }

            // Tạo đối tượng Product
            Product product = new Product();
            product.setName(productRequest.getName());
            product.setSku(productRequest.getSku());
            product.setDescription(productRequest.getDescription());
            product.setShortDescription(productRequest.getShortDescription());
          //  product.setPrice(productRequest.getPrice());
        //    product.setSalePrice(productRequest.getSalePrice());
            product.setStatus(productRequest.getStatus());

            // Lưu Product vào DB
            product = productService.save(product);

            // Kiểm tra danh sách biến thể có null không trước khi lặp
            List<VariantRequest> variants = productRequest.getVariants();
            if (variants != null && !variants.isEmpty()) {
                for (VariantRequest variantRequest : variants) {
                    AttributeTerm variant = new AttributeTerm();
                    variant.setName(variantRequest.getName());
                 //   variant.setProduct(product); // Liên kết với sản phẩm

                    attributeTermRepository.save(variant);
                }
            }

            return "redirect:/products/dashboard";

        } catch (Exception e) {
            e.printStackTrace(); // Debug lỗi trong console
            return "redirect:/error";
        }
    }









    // Hàm tạo tổ hợp tất cả các biến thể có thể có
    private List<List<String>> generateCombinations(List<List<String>> lists, int index, List<String> current) {
        List<List<String>> result = new ArrayList<>();
        if (index == lists.size()) {
            result.add(new ArrayList<>(current));
            return result;
        }

        for (String value : lists.get(index)) {
            current.add(value.trim());
            result.addAll(generateCombinations(lists, index + 1, current));
            current.remove(current.size() - 1);
        }
        return result;
    }


}