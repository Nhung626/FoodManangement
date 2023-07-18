package com.hivetech.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

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
