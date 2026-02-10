package com.restapi.controller.userprofile;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.restapi.model.userprofile.UserProfile;
import com.restapi.util.ApiResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserProfileController {

    private List<UserProfile> userProfiles = new ArrayList<>();
    private Long nextId = 6L;

    public UserProfileController() {
        userProfiles.add(new UserProfile(1L, "john_doe", "john@example.com", "John Doe", 28, "USA", "Software Developer passionate about coding", true));
        userProfiles.add(new UserProfile(2L, "jane_smith", "jane@example.com", "Jane Smith", 32, "Canada", "Data Scientist and AI enthusiast", true));
        userProfiles.add(new UserProfile(3L, "bob_wilson", "bob@example.com", "Bob Wilson", 45, "USA", "Project Manager with 15 years experience", true));
        userProfiles.add(new UserProfile(4L, "alice_brown", "alice@example.com", "Alice Brown", 26, "UK", "Full-stack developer", false));
        userProfiles.add(new UserProfile(5L, "charlie_davis", "charlie@example.com", "Charlie Davis", 35, "Australia", "DevOps Engineer", true));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<UserProfile>>> getAllUsers() {
        ApiResponse<List<UserProfile>> response = new ApiResponse<>(true, "Users retrieved successfully", userProfiles);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserById(@PathVariable Long userId) {
        Optional<UserProfile> user = userProfiles.stream()
                .filter(u -> u.getUserId().equals(userId))
                .findFirst();

        if (user.isPresent()) {
            ApiResponse<UserProfile> response = new ApiResponse<>(true, "User retrieved successfully", user.get());
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse<>(false, "User with ID " + userId + " not found", null));
    }

    @GetMapping("/search/username")
    public ResponseEntity<?> searchByUsername(@RequestParam String username) {
        Optional<UserProfile> user = userProfiles.stream()
                .filter(u -> u.getUsername().equalsIgnoreCase(username))
                .findFirst();

        if (user.isPresent()) {
            ApiResponse<UserProfile> response = new ApiResponse<>(true, "User found", user.get());
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse<>(false, "User with username " + username + " not found", null));
    }

    @GetMapping("/country/{country}")
    public ResponseEntity<?> getUsersByCountry(@PathVariable String country) {
        List<UserProfile> results = userProfiles.stream()
                .filter(user -> user.getCountry().equalsIgnoreCase(country))
                .collect(Collectors.toList());

        if (results.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, "No users found in " + country, null));
        }
        ApiResponse<List<UserProfile>> response = new ApiResponse<>(true, "Users from " + country + " retrieved successfully", results);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/age-range")
    public ResponseEntity<?> getUsersByAgeRange(@RequestParam int minAge, @RequestParam int maxAge) {
        List<UserProfile> results = userProfiles.stream()
                .filter(user -> user.getAge() >= minAge && user.getAge() <= maxAge)
                .collect(Collectors.toList());

        if (results.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, "No users found in age range " + minAge + "-" + maxAge, null));
        }
        ApiResponse<List<UserProfile>> response = new ApiResponse<>(true, "Users in age range retrieved successfully", results);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<UserProfile>> createUser(@RequestBody UserProfile userProfile) {
        userProfile.setUserId(nextId++);
        userProfile.setActive(true);
        userProfiles.add(userProfile);
        ApiResponse<UserProfile> response = new ApiResponse<>(true, "User profile created successfully", userProfile);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<?> updateUser(@PathVariable Long userId, @RequestBody UserProfile updatedUser) {
        Optional<UserProfile> existingUser = userProfiles.stream()
                .filter(user -> user.getUserId().equals(userId))
                .findFirst();

        if (existingUser.isPresent()) {
            UserProfile user = existingUser.get();
            user.setUsername(updatedUser.getUsername());
            user.setEmail(updatedUser.getEmail());
            user.setFullName(updatedUser.getFullName());
            user.setAge(updatedUser.getAge());
            user.setCountry(updatedUser.getCountry());
            user.setBio(updatedUser.getBio());
            ApiResponse<UserProfile> response = new ApiResponse<>(true, "User profile updated successfully", user);
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse<>(false, "User with ID " + userId + " not found", null));
    }

    @PatchMapping("/{userId}/activate")
    public ResponseEntity<?> activateUser(@PathVariable Long userId) {
        Optional<UserProfile> existingUser = userProfiles.stream()
                .filter(user -> user.getUserId().equals(userId))
                .findFirst();

        if (existingUser.isPresent()) {
            UserProfile user = existingUser.get();
            user.setActive(true);
            ApiResponse<UserProfile> response = new ApiResponse<>(true, "User profile activated successfully", user);
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse<>(false, "User with ID " + userId + " not found", null));
    }

    @PatchMapping("/{userId}/deactivate")
    public ResponseEntity<?> deactivateUser(@PathVariable Long userId) {
        Optional<UserProfile> existingUser = userProfiles.stream()
                .filter(user -> user.getUserId().equals(userId))
                .findFirst();

        if (existingUser.isPresent()) {
            UserProfile user = existingUser.get();
            user.setActive(false);
            ApiResponse<UserProfile> response = new ApiResponse<>(true, "User profile deactivated successfully", user);
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse<>(false, "User with ID " + userId + " not found", null));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable Long userId) {
        boolean removed = userProfiles.removeIf(user -> user.getUserId().equals(userId));
        if (removed) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse<>(false, "User with ID " + userId + " not found", null));
    }
}
