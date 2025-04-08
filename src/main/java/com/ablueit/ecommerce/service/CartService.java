package com.ablueit.ecommerce.service;

import com.ablueit.ecommerce.model.Cart;
import com.ablueit.ecommerce.model.CartItem;
import com.ablueit.ecommerce.model.Product;
import com.ablueit.ecommerce.model.User;
import com.ablueit.ecommerce.repository.CartRepository;
import com.ablueit.ecommerce.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CartService {
    @Autowired
    private CartRepository cartRepo;
    @Autowired
    private ProductRepository productRepo;

    public Cart getCartForUser(User user) {
        return cartRepo.findByUser(user).orElseGet(() -> {
            Cart cart = new Cart();
            cart.setUser(user);
            return cartRepo.save(cart);
        });
    }

    public void addToCart(User user, Long productId, int quantity) {
        Cart cart = getCartForUser(user);
        Product product = productRepo.findById(productId).orElseThrow();
        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(i -> i.getProduct().getId().equals(productId)).findFirst();

        if (existingItem.isPresent()) {
            existingItem.get().setQuantity(existingItem.get().getQuantity() + quantity);
        } else {
            CartItem newItem = new CartItem();
            newItem.setProduct(product);
            newItem.setQuantity(quantity);
            newItem.setCart(cart);
            cart.getItems().add(newItem);
        }

        cartRepo.save(cart);
    }

    public void removeItem(User user, Long itemId) {
        Cart cart = getCartForUser(user);
        cart.getItems().removeIf(i -> i.getId().equals(itemId));
        cartRepo.save(cart);
    }

    public void clearCart(User user) {
        Cart cart = getCartForUser(user);
        cart.getItems().clear();
        cartRepo.save(cart);
    }
}
