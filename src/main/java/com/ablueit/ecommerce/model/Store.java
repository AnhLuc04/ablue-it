package com.ablueit.ecommerce.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Builder
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "stores")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Store extends AbstractEntity<Long> {

    @Column(unique = true, nullable = false)
    String name;

    @Column(unique = true, nullable = false)
    String email;

    @Column(nullable = false)
    String phone;

    @Column(nullable = false)
    String address;

    @ManyToOne
    User createdBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id")
    private User seller;

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Category> categories;

}
