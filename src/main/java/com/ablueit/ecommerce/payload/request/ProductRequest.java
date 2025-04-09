package com.ablueit.ecommerce.payload.request;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.ToString;

import java.util.List;

public record ProductRequest(
        @JsonProperty("productName") String name,
        @JsonProperty("productDescription") String description,
        @JsonProperty("productShortDescription") String shortDescription,
        @JsonProperty("regularPrice") Double regularPrice,
        @JsonProperty("salePrice") Double salePrice,
        @JsonProperty("category") String category,
        @JsonProperty("storeId") Long storeId,
        @JsonProperty("sku") String sku,
        @JsonProperty("stockQuantity") Integer stockQuantity,
        @JsonProperty("stockStatus") String stockStatus,
        @JsonProperty("variationsData") List<VariationRequest> variationsData,
        @ToString.Exclude @JsonProperty("primaryImage") String primaryImage,
        @ToString.Exclude @JsonProperty("sizeGuideImage") String sizeGuideImage,
        @ToString.Exclude @JsonProperty("galleryImages") String[] galleryImages
) {
}
