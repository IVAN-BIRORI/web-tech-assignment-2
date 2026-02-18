package com.restapi.controller.ecommerce;

import com.restapi.model.ecommerce.Product;
import com.restapi.service.ecommerce.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer limit) {
        
        if (page != null && limit != null) {
            Page<Product> productPage = productService.getProductsWithPagination(page, limit);
            return ResponseEntity.ok(productPage.getContent());
        }
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable Long id) {
        return productService.getProductById(id)
                .map(product -> ResponseEntity.ok((Object) product))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Product with ID " + id + " not found"));
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<Product>> getProductsByCategory(@PathVariable String category) {
        List<Product> products = productService.getProductsByCategory(category);
        if (products.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(products);
        }
        return ResponseEntity.ok(products);
    }

    @GetMapping("/brand/{brand}")
    public ResponseEntity<List<Product>> getProductsByBrand(@PathVariable String brand) {
        List<Product> products = productService.getProductsByBrand(brand);
        if (products.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(products);
        }
        return ResponseEntity.ok(products);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Product>> searchProducts(@RequestParam String keyword) {
        List<Product> products = productService.searchProducts(keyword);
        if (products.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(products);
        }
        return ResponseEntity.ok(products);
    }

    @GetMapping("/price-range")
    public ResponseEntity<List<Product>> getProductsByPriceRange(
            @RequestParam Double min,
            @RequestParam Double max) {
        List<Product> products = productService.getProductsByPriceRange(min, max);
        if (products.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(products);
        }
        return ResponseEntity.ok(products);
    }

    @GetMapping("/filter")
    public ResponseEntity<List<Product>> filterByPriceAndBrand(
            @RequestParam Double price,
            @RequestParam String brand) {
        List<Product> products = productService.filterByPriceAndBrand(price, brand);
        if (products.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(products);
        }
        return ResponseEntity.ok(products);
    }

    @GetMapping("/in-stock")
    public ResponseEntity<List<Product>> getInStockProducts() {
        List<Product> products = productService.getInStockProducts();
        if (products.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(products);
        }
        return ResponseEntity.ok(products);
    }

    @PostMapping
    public ResponseEntity<?> addProduct(@RequestBody Product product) {
        if (productService.existsByName(product.getName())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Product with name '" + product.getName() + "' already exists");
        }
        Product savedProduct = productService.addProduct(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProduct);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        return productService.updateProduct(id, product)
                .map(updatedProduct -> ResponseEntity.ok((Object) updatedProduct))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Product with ID " + id + " not found"));
    }

    @PatchMapping("/{id}/stock")
    public ResponseEntity<?> updateStock(@PathVariable Long id, @RequestParam Integer quantity) {
        return productService.updateStockQuantity(id, quantity)
                .map(product -> ResponseEntity.ok((Object) product))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Product with ID " + id + " not found"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        if (productService.deleteProduct(id)) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Product with ID " + id + " not found");
    }
}
