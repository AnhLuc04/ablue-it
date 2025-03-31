package com.ablueit.ecommerce.payload.request;

import com.ablueit.ecommerce.model.Product;
import com.ablueit.ecommerce.model.Variant;
import com.ablueit.ecommerce.repository.ProductRepository;
import com.ablueit.ecommerce.repository.VariantRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final VariantRepository variantRepository;

    @Transactional
    public Product addProductWithVariants(String name, String sku, Double price, Double salePrice, String status, List<VariantRequest> variantDTOs) {
        Product product = new Product();
        product.setName(name);
        product.setSku(sku);
      //  product.setPrice(price);
      //  product.setSalePrice(salePrice);
        product.setStatus(status);

        List<Variant> variants = variantDTOs.stream().map(dto -> {
            Variant variant = new Variant();
            variant.setType(dto.getType());
            variant.setValue(dto.getValue());
      //      variant.setPrice(dto.getPrice());
       //     variant.setListPrice(dto.getListPrice());
            variant.setQuantity(dto.getQuantity());
            variant.setProduct(product);
            return variant;
        }).collect(Collectors.toList());

        product.setVariants(variants);
        return productRepository.save(product);
    }
}
