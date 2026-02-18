package com.restapi.service.ecommerce;

import com.restapi.model.ecommerce.Product;
import com.restapi.repository.ecommerce.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Page<Product> getProductsWithPagination(int page, int limit) {
        Pageable pageable = PageRequest.of(page, limit);
        return productRepository.findAll(pageable);
    }

    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategory(category);
    }

    public List<Product> getProductsByBrand(String brand) {
        return productRepository.findByBrand(brand);
    }

    public List<Product> searchProducts(String keyword) {
        return productRepository.searchByKeyword(keyword);
    }

    public List<Product> getProductsByPriceRange(Double min, Double max) {
        return productRepository.findByPriceBetween(min, max);
    }

    public List<Product> filterByPriceAndBrand(Double price, String brand) {
        return productRepository.findByPriceAndBrand(price, brand);
    }

    public List<Product> getInStockProducts() {
        return productRepository.findByStockQuantityGreaterThan(0);
    }

    public Product addProduct(Product product) {
        return productRepository.save(product);
    }

    public boolean existsByName(String name) {
        return productRepository.existsByName(name);
    }

    public Optional<Product> updateProduct(Long id, Product updatedProduct) {
        return productRepository.findById(id).map(existingProduct -> {
            existingProduct.setName(updatedProduct.getName());
            existingProduct.setDescription(updatedProduct.getDescription());
            existingProduct.setPrice(updatedProduct.getPrice());
            existingProduct.setCategory(updatedProduct.getCategory());
            existingProduct.setStockQuantity(updatedProduct.getStockQuantity());
            existingProduct.setBrand(updatedProduct.getBrand());
            return productRepository.save(existingProduct);
        });
    }

    public Optional<Product> updateStockQuantity(Long id, Integer quantity) {
        return productRepository.findById(id).map(product -> {
            product.setStockQuantity(quantity);
            return productRepository.save(product);
        });
    }

    public boolean deleteProduct(Long id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
