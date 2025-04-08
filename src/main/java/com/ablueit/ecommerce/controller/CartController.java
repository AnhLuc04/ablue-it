package com.ablueit.ecommerce.controller;

import ch.qos.logback.core.model.Model;
import com.ablueit.ecommerce.model.Cart;
import com.ablueit.ecommerce.model.User;
import com.ablueit.ecommerce.repository.UserRepository;
import com.ablueit.ecommerce.service.CartService;

import com.ablueit.ecommerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.Optional;

@Controller
@RequestMapping("/cart")
public class CartController {
    @Autowired
    private CartService cartService;
    @Autowired
    private UserRepository userService;

    @GetMapping
    public String showCart(Model model, Principal principal) {
        Optional<User> user = userService.findByUsername(principal.getName());
        Cart cart = cartService.getCartForUser(user.get());
       // model.addAttribute("cart", cart);
        return "cart/view";
    }

    @PostMapping("/add")
    public String addToCart(@RequestParam Long productId, @RequestParam int quantity, Principal principal) {
        Optional<User> user = userService.findByUsername(principal.getName());
        cartService.addToCart(user.get(), productId, quantity);
        return "redirect:/cart";
    }

    @PostMapping("/remove")
    public String removeItem(@RequestParam Long itemId, Principal principal) {
        Optional<User> user = userService.findByUsername(principal.getName());
        cartService.removeItem(user.get(), itemId);
        return "redirect:/cart";
    }
}
