package com.hivetech.service.interfaces;

import com.hivetech.dto.CreateProductDto;
import com.hivetech.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public interface ProductService {
    Product addProduct(CreateProductDto product) throws IOException;

    Page<Product> getAllProducts(Integer pageNo, Integer pageSize);

    Long saveUploadedFiles(MultipartFile file) throws IOException;

    List<Product> searchProduct(String keyword, Long categoryId);
}
