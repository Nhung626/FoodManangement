package com.hivetech.controller;

import com.hivetech.dto.CreateProductDto;
import com.hivetech.entity.Category;
import com.hivetech.entity.Product;
import com.hivetech.service.interfaces.CategoryService;
import com.hivetech.service.interfaces.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    private final CategoryService categoryService;

    @RequestMapping(value = "/product", method = RequestMethod.GET)
    public ModelAndView showCreateProduct() {
        ModelAndView model = new ModelAndView("private/admin/product");
        List<Category> categories = categoryService.getCategories();
        model.addObject("categories", categories);
        return model;
    }

    @RequestMapping(value = "/product", method = RequestMethod.POST)
    public ResponseEntity<Object> createProduct(
            @RequestParam("image") MultipartFile img,
            @RequestParam("name") String name,
            @RequestParam("category") int categoryId,
            @RequestParam("price") float price,
            @RequestParam("shortDescription") String shortDescription,
            @RequestParam("status") String status) throws IOException {
        CreateProductDto productDto = CreateProductDto.builder()
                .image(img)
                .name(name)
                .categoryId(categoryId)
                .price(price)
                .shortDescription(shortDescription)
                .status(status).build();

        productService.addProduct(productDto);
        return ResponseEntity.status(201).build();
    }

    @RequestMapping(value = "/products", method = RequestMethod.GET)
    public String getViewProducts() {
        return "private/admin/list-products";
    }

    @RequestMapping(value = "/api/v1/products", method = RequestMethod.GET)
    public ResponseEntity getProducts(@RequestParam(defaultValue = "0") Integer pageNo,
                                      @RequestParam(defaultValue = "5") Integer pageSize) {
        Page<Product> products = productService.getAllProducts(pageNo, pageSize);
        return ResponseEntity.ok(products);
    }

    @GetMapping(value = "/search-product")
    public ModelAndView showSearchProduct() {
        ModelAndView model = new ModelAndView("public/search-product");
        List<Category> categories = categoryService.getCategories();
        model.addObject("categories", categories);
        return model;
    }

    @PostMapping(value = "/api/v1/search-product")
    public ResponseEntity getProduct(@RequestParam(value = "keyword", required = false) String keyword, @RequestParam(value = "category", required = false) Long categoryId) {
        List<Product> products = productService.searchProduct(keyword, categoryId);
        for(Product product:products){
            String url = MvcUriComponentsBuilder
                    .fromMethodName(ProductController.class, "getImage", product.getThumbnailImage().replace("\\","/")).build().toString();
            product.setThumbnailImage(url);
        }
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @RequestMapping("/images")
    public ResponseEntity<Resource> getImage(@RequestParam("thumbnailImage") String filename) throws URISyntaxException, IOException {
        try {
            Path path = Paths.get(filename);
            Resource resource = new UrlResource(path.toUri());
            if (resource.exists()) {
                String contentType = "image/jpeg";
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_TYPE, contentType)
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }
}