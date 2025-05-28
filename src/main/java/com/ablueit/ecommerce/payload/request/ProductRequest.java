package com.ablueit.ecommerce.payload.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@NoArgsConstructor
public class ProductRequest {
    private String name;
    private String description;
    private String shortDescription;
    private Double regularPrice;
    private Double salePrice;
    private Long categoryId;
    private Long storeId;
    private String sku;
    private Integer stockQuantity;
    private String stockStatus;
    private List<VariationRequest> variationsData;
}

