package com.hivetech.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;
    private String name;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private Category category;
    private float price;
    private Long imageId;
    private String shortDescription;
    private String status;
}
