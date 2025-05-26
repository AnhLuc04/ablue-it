package com.ablueit.ecommerce.payload.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class ProductPreviewResponse {
    Long id;
    String name;
    Double price;
    String images;
    Double rating;
    Long reviews;
}
