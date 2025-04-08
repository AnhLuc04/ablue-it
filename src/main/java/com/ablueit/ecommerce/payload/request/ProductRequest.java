package com.ablueit.ecommerce.payload.request;

<<<<<<< HEAD
import java.util.List;

public class ProductRequest {
    private String name;
    private String sku;
    private String status;
    private double basePrice;
    private List<ProductVariantRequest> variants;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(double basePrice) {
        this.basePrice = basePrice;
    }

    public List<ProductVariantRequest> getVariants() {
        return variants;
    }

    public void setVariants(List<ProductVariantRequest> variants) {
        this.variants = variants;
    }
// Getters & Setters
=======
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
>>>>>>> dev-vandunxg
}
