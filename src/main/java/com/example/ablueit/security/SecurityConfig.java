package com.example.ablueit.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity

public class SecurityConfig {

    private final UserDetailsService userDetailsService;

    public SecurityConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/seller/**").hasRole("SELLER")
                        .requestMatchers("/user/**").hasRole("USER")
                        .anyRequest().authenticated()
                )
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(withDefaults());


        return http.build();
    }


    @Bean
    public AuthenticationManager authenticationManager(PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);

        return new ProviderManager(List.of(authProvider));
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}






//
//
//
//
//
//package com.example.ablueit.security;
//
//        import lombok.RequiredArgsConstructor;
//        import org.springframework.context.annotation.Bean;
//        import org.springframework.context.annotation.Configuration;
//        import org.springframework.security.authentication.AuthenticationManager;
//        import org.springframework.security.authentication.ProviderManager;
//        import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
//        import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//        import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//        import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
//        import org.springframework.security.core.userdetails.UserDetailsService;
//        import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//        import org.springframework.security.crypto.password.PasswordEncoder;
//        import org.springframework.security.web.SecurityFilterChain;
//        import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
//
//        import java.util.List;
//
//        import static org.springframework.security.config.Customizer.withDefaults;
//
//@Configuration
//@EnableWebSecurity
//public class SecurityConfig {
//
//    private final UserDetailsService userDetailsService;
//
//    public SecurityConfig(UserDetailsService userDetailsService) {
//        this.userDetailsService = userDetailsService;
//    }
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/admin/**").hasRole("ADMIN")
//                        .requestMatchers("/seller/**").hasRole("SELLER")
//                        .requestMatchers("/user/**").hasRole("USER")
//                        .requestMatchers("/", "/login", "/register").permitAll()  // Cho phép tất cả truy cập trang login và register
//                        .anyRequest().authenticated()
//                )
//                .csrf(AbstractHttpConfigurer::disable)
//                .formLogin(form -> form
//                        .loginPage("/login")  // Trang login tùy chỉnh
//                        .permitAll()
//                        .successHandler(authenticationSuccessHandler())  // Chuyển hướng tùy chỉnh sau khi đăng nhập
//                )
//                .httpBasic(withDefaults())  // Không sử dụng HTTP Basic Authentication
//                .logout(logout -> logout
//                        .logoutUrl("/logout")
//                        .permitAll()
//                );
//
//        return http.build();
//    }
//
//    @Bean
//    public AuthenticationManager authenticationManager(PasswordEncoder passwordEncoder) {
//        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
//        authProvider.setUserDetailsService(userDetailsService);
//        authProvider.setPasswordEncoder(passwordEncoder);
//
//        return new ProviderManager(List.of(authProvider));
//    }
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    @Bean
//    public AuthenticationSuccessHandler authenticationSuccessHandler() {
//        return (request, response, authentication) -> {
//            // Lấy vai trò của người dùng
//            String role = authentication.getAuthorities().stream()
//                    .map(grantedAuthority -> grantedAuthority.getAuthority())
//                    .findFirst()
//                    .orElse("");
//
//            // Chuyển hướng đến trang tương ứng dựa trên vai trò
//            if (role.equals("ROLE_ADMIN")) {
//                response.sendRedirect("/admin/dashboard");  // Chuyển hướng tới trang Admin
//            } else if (role.equals("ROLE_SELLER")) {
//                response.sendRedirect("/seller/dashboard");  // Chuyển hướng tới trang Seller
//            } else if (role.equals("ROLE_USER")) {
//                response.sendRedirect("/user/dashboard");  // Chuyển hướng tới trang User
//            } else {
//                response.sendRedirect("/");  // Nếu không có vai trò xác định, chuyển hướng về trang chủ
//            }
//        };
//    }
//}
