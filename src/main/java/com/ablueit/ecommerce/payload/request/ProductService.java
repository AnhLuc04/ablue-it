package com.ablueit.ecommerce.payload.request;

import com.ablueit.ecommerce.model.Product;
import com.ablueit.ecommerce.model.ProductVariant;
import com.ablueit.ecommerce.payload.request.ProductRequest;
import com.ablueit.ecommerce.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public Product saveProduct(ProductRequest productRequest) {
        Product product = new Product();
        product.setName(productRequest.getName());
        product.setSku(productRequest.getSku());
        //   product.setBasePrice(productRequest.getBasePrice());
        product.setStatus(productRequest.getStatus());

        List<ProductVariant> variants = productRequest.getVariants().stream().map(vr -> {
            ProductVariant variant = new ProductVariant();
            variant.setSku(vr.getSku());
            variant.setPrice(vr.getPrice());
            variant.setSalePrice(vr.getSalePrice());
            variant.setAttributes(vr.getAttributes());
            variant.setParent(product);
            return variant;
        }).collect(Collectors.toList());

        product.setVariants(variants);
        return productRepository.save(product);
    }
}
