package com.ablueit.ecommerce.service.impl;

import com.ablueit.ecommerce.enums.ImageType;
import com.ablueit.ecommerce.enums.ProductStatus;
import com.ablueit.ecommerce.enums.StockStatus;
import com.ablueit.ecommerce.exception.ResourceNotFoundException;
import com.ablueit.ecommerce.model.*;
import com.ablueit.ecommerce.payload.request.ProductRequest;
import com.ablueit.ecommerce.payload.request.VariationRequest;
import com.ablueit.ecommerce.payload.response.AttributeResponse;
import com.ablueit.ecommerce.payload.response.ProductResponse;
import com.ablueit.ecommerce.payload.response.VariationResponse;
import com.ablueit.ecommerce.repository.*;
import com.ablueit.ecommerce.service.ProductService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "PRODUCT-SERVICE")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductServiceImpl implements ProductService {

    CategoriesRepository categoriesRepository;
    AttributeRepository attributeRepository;
    AttributeTermRepository attributeTermRepository;
    ProductRepository productRepository;
    StoreRepository storeRepository;
    VariationRepository variationRepository;
    ProductImageRepository productImageRepository;

    String FOLDER_UPLOAD = "/home/vandunxg/Documents/resource_image";
    String WEB_URL = "http://localhost:8081/uploads/";

    @Override
    public String addVariationProduct(ProductRequest request) throws IOException {
//        log.info("addVariationProduct={}", request.toString());

        Store store = getStoreById(request.storeId());

        // create new product
        Product product = Product.builder()
                .name(request.name())
                .shortDescription(request.shortDescription())
                .description(request.description())
                .sku(request.sku())
                .price(request.regularPrice())
                .store(store)
                .status(ProductStatus.PUBLISHED)
                .stockQuantity(request.stockQuantity())
                .stockStatus(StockStatus.valueOf(request.stockStatus()))
                .regularPrice(request.regularPrice())
                .build();

        List<VariationRequest> variationFromRequest = request.variationsData();

        List<Variation> variations = variationFromRequest.stream().map(variationRequest -> {
            Variation variation = Variation.builder()
                    .stockQuantity(variationRequest.stock())
                    .price(variationRequest.price())
                    .build();

            List<VariationRequest.AttributeRequest> attributeRequests = variationRequest.attributes();

            List<VariationAttribute> variationAttributes = attributeRequests.stream().map(attributeRequest -> {
                Attribute attribute = getAttributeByNameOrElseCreateNew(attributeRequest.name());

                attributeRepository.save(attribute);

                AttributeTerm attributeTerm = getAttributeTermByNameOrElseCreateNew(attributeRequest.term(), attribute);

                attributeTermRepository.save(attributeTerm);

                return VariationAttribute.builder()
                        .variation(variation)
                        .attributeTerm(attributeTerm)
                        .build();
            }).toList();

            variation.setAttributes(variationAttributes);
            variation.setProduct(product);

            return variation;
        }).toList();

        Categories categories = getCategoryByName(request.category());

        List<ProductImage> productImages = new ArrayList<>();

//        productImages.add(uploadImage(request.primaryImage(),product, ImageType.PRIMARY));
//        productImages.add(uploadImage(request.sizeGuideImage(), product, ImageType.SIZE_GUIDE));
//
//        for (String x : request.galleryImages()) {
//            productImages.add(uploadImage(x, product, ImageType.DEFAULT));
//        }

        product.setCategories(List.of(categories));
        product.setProductImages(productImages);
        product.setVariations(variations);
        productRepository.save(product);

        log.info("for debug");

        return "ok";
    }

    @Override
    public ProductResponse getProduct(Long id) {
        log.info("getProduct={}", id);

        Product product = getProductById(id);

        List<VariationResponse> variationResponses = product.getVariations().stream().map(x -> {
            List<AttributeResponse> attributeResponses = x.getAttributes().stream()
                    .map(variationAttribute -> {
                        return AttributeResponse.builder()
                                .name(variationAttribute.getAttributeTerm().getAttribute().getName())
                                .term(variationAttribute.getAttributeTerm().getName())
                                .build();
                    }).toList();
            return VariationResponse.builder()
                    .attributes(attributeResponses)
                    .price(x.getPrice())
                    .stock(Long.valueOf(x.getStockQuantity()))
                    .build();
        }).toList();

        return ProductResponse.builder()
                .productName(product.getName())
                .productDescription(product.getDescription())
                .productShortDescription(product.getShortDescription())
                .regularPrice(product.getRegularPrice())
                .salePrice(product.getSalePrice())
//                .category(category)
                .sku(product.getSku())
                .variationsData(variationResponses)
                .stockQuantity(product.getStockQuantity().longValue())
                .stockStatus(product.getStockStatus().name())
//                .backorders(product.getBackOrderAllowed() ? "yes" : "no")
                .build();
    }

    private Product getProductById(Long id) {
        log.info("getProductById={}", id);

        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("product not found"));
    }

    private ProductImage uploadImage(String base64Image, Product product, ImageType type) throws IOException {
        try {
            if (base64Image.contains(",")) {
                base64Image = base64Image.split(",")[1];
            }

            byte[] imageBytes = Base64.getDecoder().decode(base64Image);


            File resourceDirectory = new File(FOLDER_UPLOAD);
            if (!resourceDirectory.exists() && !resourceDirectory.mkdirs()) {
                throw new IOException("cant create folder");
            }

            String fileName = UUID.randomUUID() + ".jpg";
            File file = new File(resourceDirectory, fileName);

            try (FileOutputStream fos = new FileOutputStream(file)) {
                log.info("write file");
                fos.write(imageBytes);
            } catch (Exception e) {
                log.error(e.getMessage());
            }

            log.info("debug");

            return ProductImage.builder()
                    .imageUrl("/uploads/" + fileName)
                    .product(product)
                    .imageType(type)
                    .build();

        } catch (IllegalArgumentException | IOException e) {
            throw new IOException("❌ Lỗi khi upload ảnh: " + e.getMessage());
        }
    }

    Categories getCategoryByName(String name) {
        log.info("getCategoryByName={}", name);

        return categoriesRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("category not found"));
    }

    AttributeTerm getAttributeTermByNameOrElseCreateNew(String name, Attribute attribute) {
        log.info("getAttributeTermByNameOrElseCreateNew={}", name);

        return attributeTermRepository.findByNameAndAttribute(name, attribute).orElseGet(() -> AttributeTerm.builder()
                .name(name)
                .attribute(attribute)
                .build());
    }

    Attribute getAttributeByNameOrElseCreateNew(String name) {
        log.info("getAttributeByNameOrElseCreateNew={}", name);

        return attributeRepository.findByName(name).orElseGet(() -> Attribute.builder()
                .name(name)
                .build());
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
