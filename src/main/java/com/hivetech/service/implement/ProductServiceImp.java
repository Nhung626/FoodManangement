package com.hivetech.service.implement;

import com.hivetech.dto.CreateProductDto;
import com.hivetech.entity.Category;
import com.hivetech.entity.Product;
import com.hivetech.exception.CustomException;
import com.hivetech.repository.CategoryRepository;
import com.hivetech.repository.ProductRepository;
import com.hivetech.service.interfaces.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class ProductServiceImp implements ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public Product addProduct(CreateProductDto productDto) {
        if (productRepository.findByName(productDto.getName()) != null) {
            throw new CustomException("Product Exist");
        }
        Category category = categoryRepository.findByCategoryId(productDto.getCategoryId());

        Product createProduct = Product.builder()
                .imageId(productDto.getImageId())
                .name(productDto.getName())
                .category(category)
                .price(productDto.getPrice())
                .shortDescription(productDto.getShortDescription())
                .status(productDto.getStatus()).build();

        productRepository.save(createProduct);
        return createProduct;
    }

    @Override
    public Page<Product> getAllProducts(Integer pageNo, Integer pageSize) {
        Pageable paging = PageRequest.of(pageNo, pageSize);
        Page<Product> productPage = productRepository.findAll(paging);
        if (productPage.hasContent()) {
            return productPage;
        } else {
            throw new CustomException("Don't have data.");
        }
    }

    public List<Product> searchProduct(String keyword, Long categoryId) {

        Category category = categoryRepository.findByCategoryId(categoryId);
        List<Product> products = productRepository.findByKeyword(keyword);
        List<Product> searchProducts = new ArrayList<>();
        if (category == null) {
            searchProducts.addAll(products);
            return searchProducts;
        }
        for (Product product : products) {
            if (product.getCategory() == category) {
                searchProducts.add(product);
            }
        }
        return searchProducts;
    }
}
