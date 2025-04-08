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
public class Cart {
    @Id @GeneratedValue
    private Long id;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> items = new ArrayList<>();

    @OneToOne
    private User user;

    // tính tổng giá trị giỏ hàng
    public double getTotal() {
        return items.stream().mapToDouble(i -> i.getProduct().getPrice() * i.getQuantity()).sum();
    }

    // thêm/xoá sản phẩm
}
