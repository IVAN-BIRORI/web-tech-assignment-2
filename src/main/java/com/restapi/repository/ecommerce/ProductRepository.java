package com.restapi.repository.ecommerce;

import com.restapi.model.ecommerce.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByCategory(String category);

    List<Product> findByBrand(String brand);

    @Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Product> searchByKeyword(@Param("keyword") String keyword);

    List<Product> findByPriceBetween(Double minPrice, Double maxPrice);

    List<Product> findByPriceAndBrand(Double price, String brand);

    List<Product> findByStockQuantityGreaterThan(Integer quantity);

    Optional<Product> findByName(String name);

    boolean existsByName(String name);
}
