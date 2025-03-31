package com.ablueit.ecommerce.service.impl;

import com.ablueit.ecommerce.exception.ResourceNotFoundException;
import com.ablueit.ecommerce.model.Product;
import com.ablueit.ecommerce.model.ProductVariation;
import com.ablueit.ecommerce.repository.ProductRepository;
import com.ablueit.ecommerce.repository.ProductVariationRepository;
import com.ablueit.ecommerce.service.ProductService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "PRODUCT-SERVICE")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductServiceImpl implements ProductService {

  ProductRepository productRepository;
  ProductVariationRepository variationRepository;

    @Transactional
    public Product addProduct(Product product) {
        if (productRepository.existsBySku(product.getSku())) {
            throw new RuntimeException("SKU đã tồn tại!");
        }

        Product savedProduct = productRepository.save(product);

        if (product.getIsVariable() && product.getVariations() != null) {
            for (ProductVariation variation : product.getVariations()) {
                variation.setParentProduct(savedProduct);
                variationRepository.save(variation);
            }
        }

        return savedProduct;
    }

  @Override
  public String showSingleProduct(Long id, Model model) {
    log.info("showSingleProduct={}", id);

    Product product = getProductById(id);

    model.addAttribute("product", product);

    return "product-dashboard/show-product";
  }

  Product getProductById(Long id) {
    log.info("getProductById={}", id);

    return productRepository
        .findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("product not found"));
  }
}
