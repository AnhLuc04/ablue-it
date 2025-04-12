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
import java.net.URL;
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
    String WEB_URL = "https://aware-only-stork.ngrok-free.app/uploads/";

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

        productImages.add(saveBase64Image(request.primaryImage(),product, ImageType.PRIMARY));
        productImages.add(saveBase64Image(request.sizeGuideImage(), product, ImageType.SIZE_GUIDE));

        for (String x : request.galleryImages()) {
            productImages.add(saveBase64Image(x, product, ImageType.DEFAULT));
        }

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

        ProductImage primaryImage = productImageRepository.findByProductAndImageType(product, ImageType.PRIMARY);
        ProductImage sizeGuide = productImageRepository.findByProductAndImageType(product, ImageType.SIZE_GUIDE);
        List<ProductImage> galleryImages = productImageRepository.findAllByProductAndImageType(product, ImageType.DEFAULT);

        return ProductResponse.builder()
                .productName(product.getName())
                .productDescription(product.getDescription())
                .productShortDescription(product.getShortDescription())
                .regularPrice(product.getRegularPrice())
                .salePrice(product.getSalePrice())
                .primaryImage(primaryImage.getImageUrl())
                .sizeGuideImage(sizeGuide.getImageUrl())
                .galleryImages(galleryImages.stream().map(ProductImage::getImageUrl).toList())
//                .category()
                .sku(product.getSku())
                .variationsData(variationResponses)
//                .stockQuantity(product.getStockQuantity().longValue())
                .stockStatus(product.getStockStatus().name())
//                .backorders(product.getBackOrderAllowed() ? "yes" : "no")
                .build();
    }

    Product getProductById(Long id) {
        log.info("getProductById={}", id);

        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("product not found"));
    }

    ProductImage saveBase64Image(String base64Image, Product product, ImageType type) throws IOException {
        if (base64Image == null || base64Image.isEmpty()) {
            return null;
        }

        String base64Data = base64Image.split(",")[1];
        byte[] imageBytes = Base64.getDecoder().decode(base64Data);

        String filename = UUID.randomUUID() + ".jpg";
        File uploadPath = new File(FOLDER_UPLOAD);
        if (!uploadPath.exists()) {
            boolean dirsCreated = uploadPath.mkdirs();
            if (!dirsCreated) {
                throw new IOException("Không thể tạo thư mục lưu trữ: " + FOLDER_UPLOAD);
            }
        }

        File file = new File(uploadPath, filename);
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(imageBytes);
        }

        return ProductImage.builder()
                .imageUrl(WEB_URL + filename)
                .product(product)
                .imageType(type)
                .build();
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
