package com.adithya.demo.controller;

import com.adithya.demo.entity.Product;
import com.adithya.demo.repository.ProductRepository;
import org.springframework.web.bind.annotation.*;
import com.adithya.demo.spec.ProductSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductRepository productRepository;

    public ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping
    public Page<Product> getAll(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            Pageable pageable) {
        Specification<Product> spec = Specification.where(ProductSpecification.hasCategory(categoryId))
                .and(ProductSpecification.hasMinPrice(minPrice))
                .and(ProductSpecification.hasMaxPrice(maxPrice));
        return productRepository.findAll(spec, pageable);
    }

    @GetMapping("/{id}")
    public Product getById(@PathVariable Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("product not found"));
    }

    @PostMapping
    public Product create(@RequestBody Product product) {
        return productRepository.save(product);
    }

    @PutMapping("/{id}")
    public Product update(@PathVariable Long id, @RequestBody Product updated) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("product not found"));
        product.setName(updated.getName());
        product.setDescription(updated.getDescription());
        product.setPrice(updated.getPrice());
        product.setStock(updated.getStock());
        product.setCategory(updated.getCategory());
        return productRepository.save(product);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        productRepository.deleteById(id);
    }
}