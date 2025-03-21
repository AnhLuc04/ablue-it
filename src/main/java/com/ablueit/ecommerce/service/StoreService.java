package com.ablueit.ecommerce.service;


import com.ablueit.ecommerce.model.Store;
import com.ablueit.ecommerce.model.User;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

public interface StoreService {
    List<Store> getStoresByUser(User user);

    String add (Store store, Model model);

    ModelAndView getDetail(Long id, ModelAndView modelAndView);

    boolean existsByName(String name);
}
