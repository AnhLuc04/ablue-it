package com.ablueit.ecommerce.payload.request;

public record StoreRequest(String name, String email, String phone, String address, String sellerId) {
}
