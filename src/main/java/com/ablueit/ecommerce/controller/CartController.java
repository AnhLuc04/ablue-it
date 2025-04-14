package com.ablueit.ecommerce.controller;

import ch.qos.logback.core.model.Model;
import com.ablueit.ecommerce.model.Cart;
import com.ablueit.ecommerce.model.CartItem;
import com.ablueit.ecommerce.model.User;
import com.ablueit.ecommerce.repository.UserRepository;
import com.ablueit.ecommerce.service.CartService;

import com.ablueit.ecommerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

//@Controller
//@RequestMapping("/cart")
//public class CartController {
//    @Autowired
//    private CartService cartService;
//    @Autowired
//    private UserRepository userService;
//
//    @GetMapping
//    public ModelAndView showCart(Principal principal) {
//        ModelAndView modelAndView = new ModelAndView();
//
//        Optional<User> userOptional = userService.findByUsername(principal.getName());
//        if (userOptional.isEmpty()) {
//            modelAndView.setViewName("redirect:/login?error=userNotFound");
//            return modelAndView;
//        }
//
//        Cart cart = cartService.getCartForUser(userOptional.get());
//
//        modelAndView.setViewName("cart/view"); // đường dẫn tới view Thymeleaf
//        modelAndView.addObject("cart", cart);  // giống với model.addAttribute()
//
//        return modelAndView;
//    }
//
//
//    @PostMapping("/add")
//    public String addToCart(@RequestParam Long productId, @RequestParam int quantity, Principal principal) {
//        Optional<User> user = userService.findByUsername(principal.getName());
//        cartService.addToCart(user.get(), productId, quantity);
//        return "redirect:/cart";
//    }
//
//    @PostMapping("/remove")
//    public String removeItem(@RequestParam Long itemId, Principal principal) {
//        Optional<User> user = userService.findByUsername(principal.getName());
//        cartService.removeItem(user.get(), itemId);
//        return "redirect:/cart";
//    }
//
//
//
//    @PostMapping("/cart/update-ajax")
//    @ResponseBody
//    public ResponseEntity<Map<String, Object>> updateCartAjax(
//            @RequestParam Long itemId,
//            @RequestParam String action,
//            Principal principal) {
//
//        // xử lý tăng/giảm số lượng, cập nhật giỏ hàng
//        Optional<User> user=userService.findByUsername(principal.getName());
//        int newQuantity = cartService.updateItemQuantity(principal.getName(), itemId, action);
//        BigDecimal totalPrice = cartService.calculateTotal(principal.getName());
//
//        Map<String, Object> response = new HashMap<>();
//        response.put("quantity", newQuantity);
//        response.put("totalPrice", totalPrice);
//
//        return ResponseEntity.ok(response);
//    }
//
//}








@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private UserRepository userRepository;


    @GetMapping
    public ModelAndView showCart(Principal principal) {
        ModelAndView modelAndView = new ModelAndView();

        Optional<User> userOptional = userRepository.findByUsername(principal.getName());
        if (userOptional.isEmpty()) {
            modelAndView.setViewName("redirect:/login?error=userNotFound");
            return modelAndView;
        }

        Cart cart = cartService.getCartForUser(userOptional.get());

        modelAndView.setViewName("cart/view"); // đường dẫn tới view Thymeleaf
        modelAndView.addObject("cart", cart);  // giống với model.addAttribute()
        modelAndView.addObject("totalPrice", cart.getTotal());

        return modelAndView;
    }

    @PostMapping("/add")
    public String addToCart(@RequestParam Long productId, @RequestParam int quantity, Principal principal) {
        User user = userRepository.findByUsername(principal.getName()).orElseThrow();
        cartService.addToCart(user, productId, quantity);
        return "redirect:/cart";
    }


    @PostMapping("/update-ajax")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> updateItemAjax(
            @RequestParam("itemId") Long itemId,
            @RequestParam("action") String action,
            Principal principal) {

        User user = userRepository.findByUsername(principal.getName()).orElseThrow();
        Cart cart = cartService.getCartForUser(user);

        cartService.updateItemQuantity(cart, itemId, action);

        BigDecimal totalPrice = cartService.calculateTotal(cart);
        int updatedQty = cartService.getQuantityForItem(cart, itemId);

        Map<String, Object> response = new HashMap<>();
        response.put("quantity", updatedQty);
        response.put("totalPrice", totalPrice);

        return ResponseEntity.ok(response);
    }




    @PostMapping("/remove-unchecked")
    @ResponseBody
    public ResponseEntity<?> removeUnchecked(@RequestBody List<Long> uncheckedIds, Principal principal) {
        User user = userRepository.findByUsername(principal.getName()).orElseThrow();
        cartService.removeUnchecked(user, uncheckedIds);
        return ResponseEntity.ok().build();
    }
}
