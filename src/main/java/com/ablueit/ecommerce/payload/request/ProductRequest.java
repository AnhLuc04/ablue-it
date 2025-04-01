package com.ablueit.ecommerce.payload.request;

import java.time.LocalDateTime;
import java.util.List;

public record ProductRequest(
        String name,
        String description,
        String shortDescription,
        String permalink,
        String price,
        String regularPrice,
        String salePrice,
        LocalDateTime dateStartSale,
        LocalDateTime dateEndSale,
        Boolean isOnSale,
        String sku,
        Long storeId,
        String stickQuantity,
        String stockStatus,
        String status,
        List<VariationRequest> variations

) {
}
