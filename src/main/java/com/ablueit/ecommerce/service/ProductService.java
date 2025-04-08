package com.ablueit.ecommerce.service;

import com.ablueit.ecommerce.payload.request.ProductRequest;

import java.io.IOException;

public interface ProductService {

    String addVariationProduct(ProductRequest request) throws IOException;
}
