//package com.ablueit.ecommerce.model;
//
//
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//
//@Entity
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//@Table(name = "attribute_term")
//public class AttributeTerm {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    private String name;
//    private String slug;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "attribute_id", nullable = false)
//    private Attribute attribute;
//}








package com.ablueit.ecommerce.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "attribute_terms")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AttributeTerm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attribute_id", nullable = false)
    private Attribute attribute;
    @ManyToMany
    @JoinTable(
            name = "product_variation_attribute_terms",
            joinColumns = @JoinColumn(name = "attribute_term_id"),
            inverseJoinColumns = @JoinColumn(name = "product_variation_id")
    )
    private List<ProductVariation> productVariations = new ArrayList<>();


}
