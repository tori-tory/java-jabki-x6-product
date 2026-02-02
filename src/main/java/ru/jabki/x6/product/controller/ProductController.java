package ru.jabki.x6.product.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.jabki.x6.product.model.Product;
import ru.jabki.x6.product.model.dto.ProductIdsRequest;
import ru.jabki.x6.product.service.ProductService;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/product")
@Tag(name = "Товары")
public class ProductController {

    private final ProductService productService;

    @PostMapping
    @Operation(summary = "Создать товар")
    public Product create(@RequestBody final Product product) {
        return productService.create(product);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить товар по id")
    public Product getById(@PathVariable("id") final Long id) {
        return productService.getById(id);
    }

    @PatchMapping
    @Operation(summary = "Изменить товар")
    public Product update(@RequestBody final Product product) {
        return productService.update(product);
    }

    @PostMapping("/check-exists")
    @Operation(summary = "Проверить существуют ли товары по списку идентификаторов")
    public ResponseEntity<Boolean> checkProductsExist(@RequestBody final ProductIdsRequest request) {
        boolean exists = productService.checkProductsExist(request.ids());
        return ResponseEntity.ok(exists);
    }
}