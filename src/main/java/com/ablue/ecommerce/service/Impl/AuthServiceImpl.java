package com.ablue.ecommerce.service.Impl;

import com.ablue.ecommerce.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "AUTH-SERVICE")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthServiceImpl implements AuthService {

    AuthenticationManager authenticationManager;

    @Override
    public String login(String username, String password, HttpServletRequest request, Model model) {
        log.info("login={}", username);

        try {

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            log.info("user={}", userDetails.getUsername());
            HttpSession session = request.getSession();
            session.setAttribute("user", userDetails);

            String role = authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .findFirst()
                    .orElse("");

            SecurityContextHolder.getContext().setAuthentication(authentication);

            return switch (role) {
                case "ROLE_ADMIN" -> {
                    log.info("in admin");
                    yield "redirect:/admin/dashboard";
                }
                case "ROLE_SELLER" -> "redirect:/seller/dashboard";
                case "ROLE_USER" -> "redirect:/user/dashboard";
                default -> "redirect:/login"; // return login if role not define in system
            };

        } catch (Exception e) {
            log.error("Error when login ={}", e.getMessage());
            model.addAttribute("errorMessage", "Sai tên đăng nhập hoặc mật khẩu");
            return "login"; // Trả về trang login nếu có lỗi
        }

    }
}
