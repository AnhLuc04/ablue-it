package com.ablueit.ecommerce.payload.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.ToString;

import java.util.List;

public record ProductRequest(
        @JsonProperty("productName") String name,
        @JsonProperty("productDescription") String description,
        @JsonProperty("productShortDescription") String shortDescription,
//        @JsonProperty("productDescription") String permalink,
//        @JsonProperty("productDescription") String price,
        @JsonProperty("regularPrice") Double regularPrice,
        @JsonProperty("salePrice") Double salePrice,
        @JsonProperty("category") String category,
        @JsonProperty("storeId") Long storeId,
//        LocalDateTime dateEndSale,
//        Boolean isOnSale,
        @JsonProperty("sku") String sku,
//        @JsonProperty("storeId") Long storeId,
        @JsonProperty("stockQuantity") Integer stockQuantity,
        @JsonProperty("stockStatus") String stockStatus,
//        @JsonProperty("status") String status,
        @JsonProperty("variationsData") List<VariationRequest> variationsData,
        @ToString.Exclude @JsonProperty("primaryImage") String primaryImage,
        @ToString.Exclude @JsonProperty("sizeGuideImage") String sizeGuideImage,
        @ToString.Exclude @JsonProperty("galleryImages") String[] galleryImages
) {
}
