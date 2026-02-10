package com.restapi.controller.restaurant;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.restapi.model.restaurant.MenuItem;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/menu")
public class MenuController {

    private List<MenuItem> menuItems = new ArrayList<>();
    private Long nextId = 9L;

    public MenuController() {
        menuItems.add(new MenuItem(1L, "Spring Rolls", "Crispy spring rolls with vegetables", 5.99, "Appetizer", true));
        menuItems.add(new MenuItem(2L, "Bruschetta", "Toasted bread with tomato and basil", 4.99, "Appetizer", true));
        menuItems.add(new MenuItem(3L, "Grilled Salmon", "Fresh salmon fillet with lemon butter sauce", 18.99, "Main Course", true));
        menuItems.add(new MenuItem(4L, "Pasta Carbonara", "Creamy pasta with bacon and parmesan", 14.99, "Main Course", true));
        menuItems.add(new MenuItem(5L, "Chocolate Lava Cake", "Warm chocolate cake with molten center", 7.99, "Dessert", true));
        menuItems.add(new MenuItem(6L, "Cheesecake", "New York style cheesecake", 6.99, "Dessert", false));
        menuItems.add(new MenuItem(7L, "Iced Coffee", "Cold brew coffee with ice", 3.99, "Beverage", true));
        menuItems.add(new MenuItem(8L, "Fresh Orange Juice", "Freshly squeezed orange juice", 4.99, "Beverage", true));
    }

    @GetMapping
    public ResponseEntity<List<MenuItem>> getAllMenuItems() {
        return ResponseEntity.ok(menuItems);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getMenuItemById(@PathVariable Long id) {
        return menuItems.stream()
                .filter(item -> item.getId().equals(id))
                .findFirst()
                .map(item -> ResponseEntity.ok((Object) item))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Menu item with ID " + id + " not found"));
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<MenuItem>> getItemsByCategory(@PathVariable String category) {
        List<MenuItem> results = menuItems.stream()
                .filter(item -> item.getCategory().equalsIgnoreCase(category))
                .collect(Collectors.toList());

        if (results.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(results);
        }
        return ResponseEntity.ok(results);
    }

    @GetMapping("/available")
    public ResponseEntity<List<MenuItem>> getAvailableItems(@RequestParam(defaultValue = "true") boolean available) {
        List<MenuItem> results = menuItems.stream()
                .filter(item -> item.isAvailable() == available)
                .collect(Collectors.toList());

        if (results.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(results);
        }
        return ResponseEntity.ok(results);
    }

    @GetMapping("/search")
    public ResponseEntity<List<MenuItem>> searchByName(@RequestParam String name) {
        List<MenuItem> results = menuItems.stream()
                .filter(item -> item.getName().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());

        if (results.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(results);
        }
        return ResponseEntity.ok(results);
    }

    @PostMapping
    public ResponseEntity<MenuItem> addMenuItem(@RequestBody MenuItem menuItem) {
        menuItem.setId(nextId++);
        menuItems.add(menuItem);
        return ResponseEntity.status(HttpStatus.CREATED).body(menuItem);
    }

    @PutMapping("/{id}/availability")
    public ResponseEntity<?> toggleAvailability(@PathVariable Long id) {
        return menuItems.stream()
                .filter(item -> item.getId().equals(id))
                .findFirst()
                .map(item -> {
                    item.setAvailable(!item.isAvailable());
                    return ResponseEntity.ok((Object) item);
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Menu item with ID " + id + " not found"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMenuItem(@PathVariable Long id) {
        boolean removed = menuItems.removeIf(item -> item.getId().equals(id));
        if (removed) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Menu item with ID " + id + " not found");
    }
}
