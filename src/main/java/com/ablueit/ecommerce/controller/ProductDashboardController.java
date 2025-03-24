package com.ablueit.ecommerce.controller;

import com.ablueit.ecommerce.exception.ResourceNotFoundException;
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

import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

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
        Store store =storeRepository.findById(storeId).orElseThrow(() -> new ResourceNotFoundException("store not found"));

        List<Categories> categories = categoryRepository.findByStore(store);
        model.addAttribute("idStore",store.getId());
        model.addAttribute("product", product);
        model.addAttribute("categories", categoryRepository);
        return "product-dashboard/create-product";
    }


    @PostMapping("/add")
    public String addProduct(
            @RequestParam("storeId") Long storeId,
            @RequestParam("files") List<MultipartFile> images,
            @RequestParam("name") String name,
            @RequestParam("sku") String sku,
            @RequestParam("description") String description,
            @RequestParam("shortDescription") String shortDescription,
            @RequestParam("price") BigDecimal price,
            @RequestParam(value = "salePrice", required = false) BigDecimal salePrice,
            @RequestParam("category") Long categoryId,
            @RequestParam("isVariable") boolean isVariable,
            @RequestParam(value = "stockQuantity", required = false) Integer stockQuantity,
            @RequestParam("status") String status,
            @RequestParam(name = "attributeNames", required = false, defaultValue = "") List<String> attributeNames,
            @RequestParam(name = "attributeValues", required = false, defaultValue = "") List<String> attributeValues) {

        // Tạo đối tượng Product và set dữ liệu
        Product product = new Product();
        product.setStore(storeRepository.findById(storeId).orElse(null));
        product.setName(name);
        product.setSku(sku);
        product.setDescription(description);
        product.setShortDescription(shortDescription);
        product.setPrice(price);
        product.setSalePrice(salePrice);
        product.setIsVariable(isVariable);
        product.setStockQuantity(stockQuantity);
        product.setStatus(status);

        // Set danh mục
        Categories category = categoryRepository.findById(categoryId).orElse(null);
        product.setCategory(category);

        // Lưu sản phẩm vào database
        productService.addProduct(product);

////        // Xử lý lưu ảnh (nếu có)
//        if (images != null && images.length > 0) {
//            for (MultipartFile image : images) {
//                if (!image.isEmpty()) {
//                    // Xử lý lưu ảnh vào server hoặc database
//                    String imageUrl = imageStorageService.saveImage(image);
//                    ImageProduct imageProduct = new ImageProduct();
//                    imageProduct.setProduct(product);
//                    imageProduct.setUrl(imageUrl);
//                    imageProductRepository.save(imageProduct);
//                }
//            }
//        }

        return "redirect:/products/add?success";
    }


}
