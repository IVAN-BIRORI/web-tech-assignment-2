package com.restapi.controller.ecommerce;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.restapi.model.ecommerce.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private List<Product> products = new ArrayList<>();
    private Long nextId = 11L;

    public ProductController() {
        products.add(new Product(1L, "Laptop Dell XPS", "High-performance laptop with Intel i7", 1299.99, "Electronics", 5, "Dell"));
        products.add(new Product(2L, "iPhone 15", "Latest Apple smartphone with 5G", 999.99, "Electronics", 10, "Apple"));
        products.add(new Product(3L, "Samsung 4K TV", "55-inch Samsung QLED 4K television", 799.99, "Electronics", 3, "Samsung"));
        products.add(new Product(4L, "Sony Headphones", "Noise-cancelling wireless headphones", 349.99, "Audio", 8, "Sony"));
        products.add(new Product(5L, "Canon Camera", "Professional DSLR camera with 24MP sensor", 1199.99, "Photography", 2, "Canon"));
        products.add(new Product(6L, "Nike Running Shoes", "Comfortable running shoes for athletes", 129.99, "Sports", 15, "Nike"));
        products.add(new Product(7L, "Adidas T-Shirt", "High-quality sports t-shirt", 49.99, "Apparel", 20, "Adidas"));
        products.add(new Product(8L, "Yoga Mat", "Non-slip yoga mat with carrying strap", 29.99, "Sports", 25, "Fitness Pro"));
        products.add(new Product(9L, "Coffee Maker", "Automatic drip coffee maker with timer", 89.99, "Home & Kitchen", 6, "Breville"));
        products.add(new Product(10L, "Gaming Mouse", "RGB gaming mouse with precision sensor", 79.99, "Electronics", 0, "Corsair"));
    }

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit) {
        int startIndex = (page - 1) * limit;
        List<Product> paginatedProducts = products.stream()
                .skip(startIndex)
                .limit(limit)
                .collect(Collectors.toList());

        return ResponseEntity.ok(paginatedProducts);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<?> getProductById(@PathVariable Long productId) {
        return products.stream()
                .filter(product -> product.getProductId().equals(productId))
                .findFirst()
                .map(product -> ResponseEntity.ok((Object) product))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Product with ID " + productId + " not found"));
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<Product>> getProductsByCategory(@PathVariable String category) {
        List<Product> results = products.stream()
                .filter(product -> product.getCategory().equalsIgnoreCase(category))
                .collect(Collectors.toList());

        if (results.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(results);
        }
        return ResponseEntity.ok(results);
    }

    @GetMapping("/brand/{brand}")
    public ResponseEntity<List<Product>> getProductsByBrand(@PathVariable String brand) {
        List<Product> results = products.stream()
                .filter(product -> product.getBrand().equalsIgnoreCase(brand))
                .collect(Collectors.toList());

        if (results.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(results);
        }
        return ResponseEntity.ok(results);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Product>> searchProducts(@RequestParam String keyword) {
        List<Product> results = products.stream()
                .filter(product -> product.getName().toLowerCase().contains(keyword.toLowerCase()) ||
                        product.getDescription().toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toList());

        if (results.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(results);
        }
        return ResponseEntity.ok(results);
    }

    @GetMapping("/price-range")
    public ResponseEntity<List<Product>> getProductsByPriceRange(
            @RequestParam Double min,
            @RequestParam Double max) {
        List<Product> results = products.stream()
                .filter(product -> product.getPrice() >= min && product.getPrice() <= max)
                .collect(Collectors.toList());

        if (results.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(results);
        }
        return ResponseEntity.ok(results);
    }

    @GetMapping("/in-stock")
    public ResponseEntity<List<Product>> getInStockProducts() {
        List<Product> results = products.stream()
                .filter(product -> product.getStockQuantity() > 0)
                .collect(Collectors.toList());

        if (results.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(results);
        }
        return ResponseEntity.ok(results);
    }

    @PostMapping
    public ResponseEntity<Product> addProduct(@RequestBody Product product) {
        product.setProductId(nextId++);
        products.add(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(product);
    }

    @PutMapping("/{productId}")
    public ResponseEntity<?> updateProduct(@PathVariable Long productId, @RequestBody Product updatedProduct) {
        return products.stream()
                .filter(product -> product.getProductId().equals(productId))
                .findFirst()
                .map(product -> {
                    product.setName(updatedProduct.getName());
                    product.setDescription(updatedProduct.getDescription());
                    product.setPrice(updatedProduct.getPrice());
                    product.setCategory(updatedProduct.getCategory());
                    product.setStockQuantity(updatedProduct.getStockQuantity());
                    product.setBrand(updatedProduct.getBrand());
                    return ResponseEntity.ok((Object) product);
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Product with ID " + productId + " not found"));
    }

    @PatchMapping("/{productId}/stock")
    public ResponseEntity<?> updateStock(@PathVariable Long productId, @RequestParam int quantity) {
        return products.stream()
                .filter(product -> product.getProductId().equals(productId))
                .findFirst()
                .map(product -> {
                    product.setStockQuantity(quantity);
                    return ResponseEntity.ok((Object) product);
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Product with ID " + productId + " not found"));
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long productId) {
        boolean removed = products.removeIf(product -> product.getProductId().equals(productId));
        if (removed) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Product with ID " + productId + " not found");
    }
}
