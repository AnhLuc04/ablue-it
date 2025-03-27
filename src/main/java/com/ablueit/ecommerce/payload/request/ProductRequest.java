package com.ablueit.ecommerce.payload.request;
import lombok.Data;
import java.util.List;

@Data
public class ProductRequest {
    private String name;
    private String sku;
    private String description;
    private String shortDescription;
    private Double price;
    private Double salePrice;
    private String status;
    private List<VariantRequest> variants;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(Double salePrice) {
        this.salePrice = salePrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<VariantRequest> getVariants() {
        return variants;
    }

    public void setVariants(List<VariantRequest> variants) {
        this.variants = variants;
    }
}
