package com.ablueit.ecommerce.service;

import com.ablueit.ecommerce.payload.request.ProductRequest;
import com.ablueit.ecommerce.payload.response.ProductPreviewResponse;
import com.ablueit.ecommerce.payload.response.ProductResponse;

import java.io.IOException;
import java.util.List;

public interface ProductService {

    String addVariationProduct(ProductRequest request) throws IOException;

    ProductResponse getProduct(Long id);

    List<ProductPreviewResponse> getAllProductByCategory(Long id);

    List<ProductPreviewResponse> getAllProductByStore(Long id);
}
