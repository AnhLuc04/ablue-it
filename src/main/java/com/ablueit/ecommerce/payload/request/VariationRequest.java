package com.ablueit.ecommerce.payload.request;

import java.util.List;

public record VariationRequest(
        String description,
        String price,
        String regularPrice,
        String salePrice,
        String stockQuantity,
        List<AttributeRequest> attributes

) {

    public record AttributeRequest(
            Long attributeId,
            Long attributeTermId
    ) {
    }

}
