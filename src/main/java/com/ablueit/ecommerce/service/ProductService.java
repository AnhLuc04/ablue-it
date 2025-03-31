package com.ablueit.ecommerce.service;

import org.springframework.ui.Model;

public interface ProductService {

  String showSingleProduct(Long id, Model model);
}
