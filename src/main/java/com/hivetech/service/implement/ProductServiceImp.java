package com.hivetech.service.implement;

import com.hivetech.dto.CreateProductDto;
import com.hivetech.entity.Category;
import com.hivetech.entity.Product;
import com.hivetech.exception.CustomException;
import com.hivetech.repository.CategoryRepository;
import com.hivetech.repository.ProductRepository;
import com.hivetech.service.interfaces.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class ProductServiceImp implements ProductService {
    @Value("${media.img-path}")
    private String UPLOADED_FOLDER;
    private final String FILE_EXTENSION = ".jpg";
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public Product addProduct(CreateProductDto productDto) throws IOException {
        if (productRepository.findByName(productDto.getName()) != null) {
            throw new CustomException("Product Exist");
        }
        String thumbnailImage = saveUploadedFiles(productDto.getImage());
        Optional<Category> category = categoryRepository.findById(Long.valueOf(productDto.getCategoryId()));

        Product createProduct = Product.builder()
                .thumbnailImage(thumbnailImage)
                .name(productDto.getName())
                .category(category.get())
                .price(productDto.getPrice())
                .shortDescription(productDto.getShortDescription())
                .status(productDto.getStatus()).build();

        productRepository.save(createProduct);
        return createProduct;
    }

    public Page<Product> getAllProducts(Integer pageNo, Integer pageSize) {
        Pageable paging = PageRequest.of(pageNo, pageSize);
        Page<Product> productPage = productRepository.findAll(paging);
        if (productPage.hasContent()) {
            return productPage;
        } else {
            throw new CustomException("Don't have data.");
        }
    }

    public String saveUploadedFiles(MultipartFile file) throws IOException {
        File dir = new File(UPLOADED_FOLDER, "images");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        Random rand = new Random();
        int ranNum = rand.nextInt();
        if (!file.isEmpty()) {
            byte[] bytes = file.getBytes();
            Path path = Paths.get(dir + "//" + file.getName() + ranNum + FILE_EXTENSION);
            Files.write(path, bytes);
            return path.toString();
        }
        return null;
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
