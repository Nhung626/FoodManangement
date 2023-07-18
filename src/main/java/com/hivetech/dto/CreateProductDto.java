package com.hivetech.dto;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class CreateProductDto {
    private String imageId;
    private String name;
    private Long categoryId;
    private float price;
    private String shortDescription;
    private String status;
}
