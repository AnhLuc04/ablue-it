package com.ablueit.ecommerce.service;

import com.ablueit.ecommerce.model.Cart;
import com.ablueit.ecommerce.model.CartItem;
import com.ablueit.ecommerce.model.Product;
import com.ablueit.ecommerce.model.User;
import com.ablueit.ecommerce.repository.CartItemRepository;
import com.ablueit.ecommerce.repository.CartRepository;
import com.ablueit.ecommerce.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    public Cart getCartForUser(User user) {
        return cartRepository.findByUser(user).orElseGet(() -> {
            Cart cart = new Cart();
            cart.setUser(user);
            return cartRepository.save(cart);
        });
    }

    public void addToCart(User user, Long productId, int quantity) {
        Cart cart = getCartForUser(user);
        Optional<CartItem> existing = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst();

        if (existing.isPresent()) {
            existing.get().setQuantity(existing.get().getQuantity() + quantity);
        } else {
            CartItem newItem = new CartItem();
            newItem.setProduct(new Product(productId)); // tạo tạm bằng ID
            newItem.setQuantity(quantity);
            newItem.setCart(cart);
            cart.getItems().add(newItem);
        }

        cartRepository.save(cart);
    }

    public void updateQuantity(User user, Long itemId, String action) {
        Cart cart = getCartForUser(user);
        cart.getItems().stream()
                .filter(item -> item.getId().equals(itemId))
                .findFirst()
                .ifPresent(item -> {
                    if ("increase".equals(action)) {
                        item.setQuantity(item.getQuantity() + 1);
                    } else if ("decrease".equals(action) && item.getQuantity() > 1) {
                        item.setQuantity(item.getQuantity() - 1);
                    }
                });

        cartRepository.save(cart);
    }
//    public Cart getCartForUser(User user) {
//        return cartRepository.findByUser(user)
//                .orElseGet(() -> {
//                    Cart newCart = new Cart();
//                    newCart.setUser(user);
//                    return cartRepository.save(newCart);
//                });
//    }

    public void updateItemQuantity(Cart cart, Long itemId, String action) {
        CartItem item = cart.getItems().stream()
                .filter(i -> i.getId().equals(itemId))
                .findFirst()
                .orElseThrow();

        if ("increase".equals(action)) {
            item.setQuantity(item.getQuantity() + 1);
        } else if ("decrease".equals(action) && item.getQuantity() > 1) {
            item.setQuantity(item.getQuantity() - 1);
        }

        cartItemRepository.save(item);
    }

    public int getQuantityForItem(Cart cart, Long itemId) {
        return cart.getItems().stream()
                .filter(i -> i.getId().equals(itemId))
                .map(CartItem::getQuantity)
                .findFirst()
                .orElse(0);
    }

    public BigDecimal calculateTotal(Cart cart) {
        if (cart == null || cart.getItems() == null) {
            return BigDecimal.ZERO;
        }

        return cart.getItems().stream()
                .map(item -> {
                    Double priceDouble = item.getProduct().getSalePrice(); // Giả sử kiểu là Double
                    int quantity = item.getQuantity();

                    BigDecimal price = priceDouble != null ? BigDecimal.valueOf(priceDouble) : BigDecimal.ZERO;
                    return price.multiply(BigDecimal.valueOf(quantity));
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    public void removeUnchecked(User user, List<Long> uncheckedIds) {
        Cart cart = getCartForUser(user);
        cart.getItems().removeIf(item -> uncheckedIds.contains(item.getId()));
        cartRepository.save(cart);
    }
}
