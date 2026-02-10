# Spring Boot REST API Project

REST API built with Spring Boot for my Web Tech class. Has 6 different APIs - books, students, menu items, products, tasks and user profiles.

## Setup

Need Java 17 and Maven installed.

```
mvn clean install
mvn spring-boot:run
```

Runs on http://localhost:8080

## APIs

### Books `/api/books`

GET /api/books - all books
GET /api/books/1 - single book
GET /api/books/search?title=code - search
POST /api/books - add book
DELETE /api/books/1 - delete

```
curl http://localhost:8080/api/books
```

### Students `/api/students`

GET /api/students
GET /api/students/1
GET /api/students/major/Computer%20Science
GET /api/students/filter?gpa=3.5
POST /api/students
PUT /api/students/1

### Menu `/api/menu`

GET /api/menu
GET /api/menu/1
GET /api/menu/category/Appetizer
GET /api/menu/available?available=true
GET /api/menu/search?name=salmon
POST /api/menu
PUT /api/menu/1/availability - toggles available
DELETE /api/menu/1

### Products `/api/products`

GET /api/products?page=1&limit=5
GET /api/products/1
GET /api/products/category/Electronics
GET /api/products/brand/Apple
GET /api/products/search?keyword=laptop
GET /api/products/price-range?min=100&max=500
GET /api/products/in-stock
POST /api/products
PUT /api/products/1
PATCH /api/products/1/stock?quantity=10
DELETE /api/products/1

Example:
```
curl "http://localhost:8080/api/products/price-range?min=50&max=200"
```

### Tasks `/api/tasks`

GET /api/tasks
GET /api/tasks/1
GET /api/tasks/status?completed=false
GET /api/tasks/priority/HIGH
POST /api/tasks
PUT /api/tasks/1
PATCH /api/tasks/1/complete
DELETE /api/tasks/1

### Users `/api/users`

Uses ApiResponse wrapper - returns {success, message, data}

GET /api/users
GET /api/users/1
GET /api/users/search/username?username=john_doe
GET /api/users/country/USA
GET /api/users/age-range?minAge=25&maxAge=35
POST /api/users
PUT /api/users/1
PATCH /api/users/1/activate
PATCH /api/users/1/deactivate
DELETE /api/users/1

Example response:
```json
{
  "success": true,
  "message": "Users retrieved successfully",
  "data": [...]
}
```

## Status Codes

200 - OK
201 - Created  
204 - Deleted
404 - Not found
