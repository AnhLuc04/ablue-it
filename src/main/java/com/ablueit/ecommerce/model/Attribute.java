package com.ablueit.ecommerce.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Entity
@Builder
@Getter
@Setter
@Table(name = "attribute")
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class Attribute extends AuditEntity<Long> {

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

    @OneToMany(mappedBy = "attribute", fetch = FetchType.LAZY)
    List<AttributeTerm> terms;
}
