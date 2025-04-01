package com.ablueit.ecommerce.service.impl;

import com.ablueit.ecommerce.enums.ProductStatus;
import com.ablueit.ecommerce.enums.StockStatus;
import com.ablueit.ecommerce.exception.ResourceNotFoundException;
import com.ablueit.ecommerce.model.*;
import com.ablueit.ecommerce.payload.request.ProductRequest;
import com.ablueit.ecommerce.payload.request.VariationRequest;
import com.ablueit.ecommerce.repository.AttributeRepository;
import com.ablueit.ecommerce.repository.AttributeTermRepository;
import com.ablueit.ecommerce.repository.ProductRepository;
import com.ablueit.ecommerce.repository.StoreRepository;
import com.ablueit.ecommerce.service.ProductService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "PRODUCT-SERVICE")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductServiceImpl implements ProductService {

    AttributeRepository attributeRepository;
    AttributeTermRepository attributeTermRepository;
    ProductRepository productRepository;
    StoreRepository storeRepository;

    @Override
    public String addVariationProduct(ProductRequest request) {
        log.info("addVariationProduct={}", request.toString());

        Store store = getStoreById(request.storeId());

        // create new product
        Product product = Product.builder()
                .name(request.name())
                .shortDescription(request.shortDescription())
                .description(request.description())
                .sku(request.sku())
                .price(request.price())
                .store(store)
                .status(ProductStatus.valueOf(request.status()))
                .stockQuantity(Integer.parseInt(request.stickQuantity()))
                .stockStatus(StockStatus.valueOf(request.stockStatus()))
                .regularPrice(request.regularPrice())
                .build();


        List<VariationRequest> variationRequests = request.variations();

        List<Variation> variations = new ArrayList<>();

        Set<Attribute> attributesForProduct = new HashSet<>();

        variationRequests.forEach(x -> {
            List<Long> attributeIdList = x.attributes().stream()
                    .map(VariationRequest.AttributeRequest::attributeId).toList();
            List<Long> attributeTermIdList = x.attributes().stream()
                    .map(VariationRequest.AttributeRequest::attributeTermId).toList();

            List<Attribute> attributes = getAllAttributeByListId(attributeIdList);
            List<AttributeTerm> attributeTerms = getAllAttributeTermByListId(attributeTermIdList);

            variations.add(Variation.builder()
                    .attributes(attributes)
                    .attributeTerms(attributeTerms)
                    .product(product)
                    .build());

            attributesForProduct.addAll(attributes);
        });

        product.setVariations(variations);
        product.setAttributes(attributesForProduct.stream().toList());
        productRepository.save(product);

        log.info("for debug");

        return "ok";
    }

    List<Attribute> getAllAttributeByListId(List<Long> id) {
        log.info("getAllAttributeByListId={}", id.toString());

        return attributeRepository.findAllById(id);
    }

    List<AttributeTerm> getAllAttributeTermByListId(List<Long> id) {
        log.info("getAllAttributeTermByListId={}", id.toString());

        return attributeTermRepository.findAllById(id);
    }

    Store getStoreById(Long id) {
        log.info("getStoreById={}", id);

        return storeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("store not found"));
    }
}
