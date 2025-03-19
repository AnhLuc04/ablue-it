package com.example.ablueit.model;



import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "stores")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    private LocalDateTime date;
    // Một cửa hàng có một chủ (Seller)
    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User seller;

    // Một cửa hàng có nhiều danh mục sản phẩm
    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL)
    private List<Category> categories;
}
