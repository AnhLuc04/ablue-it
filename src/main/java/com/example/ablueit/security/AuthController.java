package com.example.ablueit.security;
;
import com.example.ablueit.model.User;
import com.example.ablueit.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @GetMapping("/login")
    public String showLoginPage(@RequestParam(value = "error", required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("errorMessage", "Sai tên đăng nhập hoặc mật khẩu");
        }
        return "login";
    }

//    @PostMapping("/process-login")
//    public String login(
//            @RequestParam(name = "username") String username,
//            @RequestParam(name = "password") String password,
//            HttpServletRequest request,
//            Model model) {
//        try {
//            Authentication authentication = authenticationManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(username, password)
//            );
//            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
//
//            // Lưu user vào session
//            HttpSession session = request.getSession();
//            session.setAttribute("user", userDetails);
//
//            String role = authentication.getAuthorities().stream()
//                    .map(grantedAuthority -> grantedAuthority.getAuthority())
//                    .findFirst()
//                    .orElse("");
//            if (role=="ROLE_ADMIN") {
//                return "redirect:/admin/dashboard"; // Chuyển hướng sau khi login thành công
//            }
//            else if (role=="ROLE_SELLER") {
//                return "redirect:/seller/dashboard"; // Chuyển hướng sau khi login thành công
//            }
//
//        } catch (Exception e) {
//            model.addAttribute("errorMessage", "Sai tên đăng nhập hoặc mật khẩu");
//            return "login"; // Trả về trang login khi có lỗi
//        }
//    }



    @PostMapping("/process-login")
    public String login(
            @RequestParam(name = "username") String username,
            @RequestParam(name = "password") String password,
            HttpServletRequest request,
            Model model) {
        try {
            // Thực hiện xác thực người dùng
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            // Lưu thông tin người dùng vào session
            HttpSession session = request.getSession();
            session.setAttribute("user", userDetails);

            // Lấy vai trò của người dùng
            String role = authentication.getAuthorities().stream()
                    .map(grantedAuthority -> grantedAuthority.getAuthority())
                    .findFirst()
                    .orElse("");

            // Chuyển hướng theo vai trò
            if ("ROLE_ADMIN".equals(role)) {
                return "redirect:/admin/dashboard"; // Chuyển hướng đến trang dashboard của Admin
            } else if ("ROLE_SELLER".equals(role)) {
                return "redirect:/seller/dashboard"; // Chuyển hướng đến trang dashboard của Seller
            } else if ("ROLE_USER".equals(role)) {
                return "redirect:/user/dashboard"; // Chuyển hướng đến trang dashboard của User
            } else {
                // Trường hợp vai trò không hợp lệ hoặc không xác định, có thể trả về trang mặc định hoặc lỗi
                return "redirect:/login"; // Hoặc trang chính nếu không có vai trò hợp lệ
            }

        } catch (Exception e) {
            model.addAttribute("errorMessage", "Sai tên đăng nhập hoặc mật khẩu");
            return "login"; // Trả về trang login nếu có lỗi
        }
    }





    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        request.getSession().invalidate();
        return "redirect:/login";
    }
}
