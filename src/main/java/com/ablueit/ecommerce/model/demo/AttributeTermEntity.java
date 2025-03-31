package com.ablueit.ecommerce.model.demo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@ToString
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "attribute_term_entity")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AttributeTermEntity {

  @Id
  @Column(name = "attribute_term_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long id;

  @Column(name = "name")
  String name;

  @Column(name = "slug")
  String slug;

  @Column(name = "description")
  String description;

  @Column(name = "count")
  Long count; // num of public product

  @ToString.Exclude
  @ManyToOne
  @JsonIgnore
  @JoinColumn(name = "attribute_id", nullable = false)
  AttributeEntity attribute;
}
