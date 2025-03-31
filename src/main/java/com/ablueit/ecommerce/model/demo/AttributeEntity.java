package com.ablueit.ecommerce.model.demo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Builder
@Getter
@Setter
@ToString
@Table(name = "attribute_entity")
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class AttributeEntity {

  @Id
  @Column(name = "attribute_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long id;

  @Column(name = "name")
  String name;

  @Column(name = "slug")
  String slug;

  @Column(name = "type")
  String type;

  @Column(name = "has_archives")
  Boolean hasArchives = false;

  @ToString.Exclude
  @JsonIgnore
  @OneToMany(mappedBy = "attribute", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  Set<AttributeTermEntity> attributeTerms = new HashSet<>();
}
