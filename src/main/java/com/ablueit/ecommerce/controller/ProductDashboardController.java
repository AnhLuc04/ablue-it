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
    @Autowired
    private ProductVariationSampleRepository productVariationSampleRepository;



    @GetMapping("/add/{storeId}")
    public String showAddProductForm(@PathVariable("storeId") Long storeId, Model model) {
        Product product = new Product();
        product.setVariations(new ArrayList<>());

        // Lấy danh sách danh mục theo storeId
        Store store = storeRepository.findById(storeId).orElseThrow(() -> new ResourceNotFoundException("store not found"));


//        List<ProductVariationSample>productVariationSamples=productVariationSampleRepository.findByStore(store);
        List<Categories> categories = categoryRepository.findByStore(store);
//        model.addAttribute("productVariationSamples", productVariationSamples);
        model.addAttribute("idStore", store.getId());
        model.addAttribute("product", product);
        model.addAttribute("categories", categories);
        return "product-dashboard/create-product";
    }









//
//    @PostMapping("/add")
//    public String addProduct(
//            @RequestParam("storeId") Long storeId,
//            @RequestParam("files") List<MultipartFile> images,
//            @RequestParam("name") String name,
//            @RequestParam("sku") String sku,
//            @RequestParam("description") String description,
//            @RequestParam("shortDescription") String shortDescription,
//            @RequestParam("price") BigDecimal price,
//            @RequestParam(value = "salePrice", required = false) BigDecimal salePrice,
//            @RequestParam("categories") Long categoryId,
//            @RequestParam("isVariable") boolean isVariable,
//            @RequestParam(value = "stockQuantity", required = false) Integer stockQuantity,
//            @RequestParam("status") String status,
//            @RequestParam(name = "variationColor", required = false) List<String> variationColors,
//            @RequestParam(name = "variationSize", required = false) List<String> variationSizes,
//            @RequestParam(name = "variationPrice", required = false) List<BigDecimal> variationPrices,
//            @RequestParam(name = "variationSalePrice", required = false) List<BigDecimal> variationSalePrices
//
//    ) {
//
//        // Tạo sản phẩm chính
//        Product product = new Product();
//        product.setStore(storeRepository.findById(storeId).orElse(null));
//        product.setName(name);
//        product.setSku(sku);
//        product.setDescription(description);
//        product.setShortDescription(shortDescription);
//        product.setPrice(price);
//        product.setSalePrice(salePrice);
//        product.setIsVariable(isVariable);
//        product.setStockQuantity(stockQuantity);
//        product.setStatus(status);
//        Categories category = categoryRepository.findById(categoryId).orElse(null);
//        product.setCategory(category);
//        productService.save(product);
//
//        if (isVariable && variationColors != null && variationSizes != null && variationPrices != null && variationSalePrices!= null) {
//            for (int i = 0; i < variationColors.size(); i++) {
//                ProductVariation variation = new ProductVariation();
//                variation.setParentProduct(product);
//                variation.setColor(variationColors.get(i));
//                variation.setSize(variationSizes.get(i));
//                variation.setPrice(variationPrices.get(i));
//                variation.setSalePrice(variationSalePrices.get(i));
//                productVariationRepository.save(variation);
//            }
//        }
//
////        for (MultipartFile file : images) {
////            if (isImageFile(file)) {
////                String fileName = file.getOriginalFilename();
////                int dotIndex = fileName.lastIndexOf('.');
////                fileName = fileName.substring(0, dotIndex);
////                String productSku = sku.replace(".", "_") + "-" + fileName;
////                // Tải hình ảnh và lấy URL
////                Map<String, String> uploadResponse = wooCommerceCreateProductService.uploadImage(file);
////                String imageLink = uploadResponse.values().iterator().next();
////
////            }
////        }
//                return "redirect:/products/add?success";
//    }





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
            @RequestParam("categories") Long categoryId,
            @RequestParam("isVariable") boolean isVariable,
            @RequestParam(value = "stockQuantity", required = false) Integer stockQuantity,
            @RequestParam("status") String status,
            @RequestParam(name = "variationColor", required = false) List<String> variationColors,
            @RequestParam(name = "variationSize", required = false) List<String> variationSizes,
            @RequestParam(name = "variationPrice", required = false) List<BigDecimal> variationPrices,
            @RequestParam(name = "variationSalePrice", required = false) List<BigDecimal> variationSalePrices
    ) {
        // Tạo sản phẩm chính
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
        Categories category = categoryRepository.findById(categoryId).orElse(null);
        product.setCategory(category);

        // **Lưu sản phẩm trước khi thêm biến thể**
        product = productService.save(product);

        // Nếu là sản phẩm biến thể thì thêm danh sách biến thể
        if (isVariable && variationColors != null && variationSizes != null && variationPrices != null && variationSalePrices != null) {
            for (int i = 0; i < variationColors.size(); i++) {
                ProductVariation variation = new ProductVariation();
                variation.setParentProduct(product);
//                variation.setColor(variationColors.get(i));
//                variation.setSize(variationSizes.get(i));
                variation.setPrice(variationPrices.get(i));
                variation.setSalePrice(variationSalePrices.get(i));

                // **Lưu biến thể**
                productVariationRepository.save(variation);
            }
        }

        return "redirect:/products/add?success";
    }





    private boolean isImageFile(MultipartFile file) {
        String contentType = file.getContentType();
        String fileName = file.getOriginalFilename();
        return (contentType.equals("image/jpeg") ||
                contentType.equals("image/png") ||
                contentType.equals("image/gif") ||
                contentType.equals("image/bmp") ||
                contentType.equals("image/webp")) ||
                (fileName != null && (fileName.endsWith(".jpg") ||
                        fileName.endsWith(".jpeg") ||
                        fileName.endsWith(".png") ||
                        fileName.endsWith(".gif") ||
                        fileName.endsWith(".bmp") ||
                        fileName.endsWith(".webp")));
    }
}