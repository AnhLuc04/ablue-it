package com.ablueit.ecommerce.service;

import com.ablueit.ecommerce.model.User;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;

public interface SellerService {

    String getDetails(Model model, Principal principal);

    ModelAndView updateProfile(ModelAndView modelAndView, User seller, Principal principal);

    void deleteStaff(Long id);

}
