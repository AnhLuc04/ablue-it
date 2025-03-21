package com.ablueit.ecommerce.service.impl;

import com.ablueit.ecommerce.model.Product;
import com.ablueit.ecommerce.model.ProductVariation;
import com.ablueit.ecommerce.repository.ProductRepository;
import com.ablueit.ecommerce.repository.ProductVariationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductServiceImpl {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductVariationRepository variationRepository;

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
}
