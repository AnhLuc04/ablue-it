package com.ablueit.ecommerce.controller;

import com.ablueit.ecommerce.payload.request.CategoryRequest;
import com.ablueit.ecommerce.service.CategoryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/category")
@Slf4j(topic = "CATEGORY-CONTROLLER")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryController {

    CategoryService categoryService;

    @PostMapping("/")
    public String create(@ModelAttribute CategoryRequest request, RedirectAttributes redirectAttributes){
        log.info("POST /category/ ={}", request.toString());

        return categoryService.create(request, redirectAttributes);
    }



}
