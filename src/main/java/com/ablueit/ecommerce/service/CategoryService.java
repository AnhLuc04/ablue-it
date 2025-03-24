package com.ablueit.ecommerce.service;

import com.ablueit.ecommerce.model.Categories;
import com.ablueit.ecommerce.model.Store;
import com.ablueit.ecommerce.payload.request.CategoryRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

public interface CategoryService {

    String create(CategoryRequest request, RedirectAttributes redirectAttributes);

    List<Categories> getCategoriesByStore(Store store);

    String delete(Long id);

}
