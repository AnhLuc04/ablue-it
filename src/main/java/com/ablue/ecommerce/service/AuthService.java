package com.ablue.ecommerce.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.ui.Model;

public interface AuthService {
    String login(String username, String password, HttpServletRequest
                 request, Model model);
}
