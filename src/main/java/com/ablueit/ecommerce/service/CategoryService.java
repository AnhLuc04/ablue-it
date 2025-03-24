package com.ablueit.ecommerce.service;

import com.ablueit.ecommerce.model.Category;
import com.ablueit.ecommerce.model.Store;
import com.ablueit.ecommerce.payload.request.CategoryRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

public interface CategoryService {

    String create(CategoryRequest request, RedirectAttributes redirectAttributes);

    List<Category> getCategoriesByStore(Store store);

    String delete(Long id);

}
