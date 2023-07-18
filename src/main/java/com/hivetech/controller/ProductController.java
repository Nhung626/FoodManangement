package com.hivetech.controller;

import com.hivetech.dto.CreateProductDto;
import com.hivetech.entity.Category;
import com.hivetech.entity.Product;
import com.hivetech.service.interfaces.CategoryService;
import com.hivetech.service.interfaces.MediaService;
import com.hivetech.service.interfaces.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ProductController {
    private final ProductService productService;
    private final CategoryService categoryService;
    private final MediaService mediaService;

    @Value("${pocketBase.host}")
    private String pocketBaseHost;
    @Value("${pocketBase.username}")
    private String pocketBaseEmail;
    @Value("${pocketBase.password}")
    private String pocketBasePassword;

    @GetMapping("/api/v1/get-value")
    @ResponseBody
    public Map returnValue() {
        return Map.of("pocketBaseHost", pocketBaseHost,
                "pocketBaseEmail", pocketBaseEmail,
                "pocketBasePassword", pocketBasePassword);
    }

    @RequestMapping(value = "/product", method = RequestMethod.GET)
    public ModelAndView showCreateProduct() {
        ModelAndView model = new ModelAndView("private/admin/product");
        List<Category> categories = categoryService.getCategories();
        model.addObject("categories", categories);
        return model;
    }

    @RequestMapping(value = "/api/v1/product", method = RequestMethod.POST)
    public ResponseEntity<Object> createProduct(@RequestBody CreateProductDto product) throws IOException {
        productService.addProduct(product);
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

    @GetMapping(value = "/api/v1/public/search-product")
    public ResponseEntity getProduct(@RequestParam(value = "keyword", required = false) String keyword, @RequestParam(value = "category", required = false) Long categoryId) {
        List<Product> products = productService.searchProduct(keyword, categoryId);
        for (Product product : products) {
            String urlImage = mediaService.getPathImage(product.getImageId());
            product.setImageId(urlImage);
        }
        return new ResponseEntity<>(products, HttpStatus.OK);
    }
}