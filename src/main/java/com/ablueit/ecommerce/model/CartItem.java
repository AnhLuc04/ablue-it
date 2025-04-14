package com.ablueit.ecommerce.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {
    @Id @GeneratedValue
    private Long id;

    @ManyToOne
    private Product product;

    private int quantity;

    @ManyToOne
    private Cart cart;
    @Transient // Không lưu trong database, chỉ dùng tạm thời để hiển thị
    private Double subtotal;

    // Getter và Setter cho subtotal
    public Double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
    }
    // getters, setters
}
