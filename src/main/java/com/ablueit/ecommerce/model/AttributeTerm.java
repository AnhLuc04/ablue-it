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
}
