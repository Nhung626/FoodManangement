package com.hivetech.service.interfaces;

import com.hivetech.dto.CreateProductDto;
import com.hivetech.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProductService {
    Product addProduct(CreateProductDto product);

    Page<Product> getAllProducts(Integer pageNo, Integer pageSize);

    List<Product> searchProduct(String keyword, Long categoryId);
}
