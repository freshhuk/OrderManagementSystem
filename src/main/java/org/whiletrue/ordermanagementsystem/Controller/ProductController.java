package org.whiletrue.ordermanagementsystem.Controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.whiletrue.ordermanagementsystem.Domain.Entity.Product;
import org.whiletrue.ordermanagementsystem.Domain.Models.CreateProductRequest;
import org.whiletrue.ordermanagementsystem.Services.ProductService;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService){
        this.productService = productService;
    }

    @PostMapping("/addProduct")
    public ResponseEntity<Product> addProduct(@RequestBody @Valid CreateProductRequest request) {
        return ResponseEntity.ok(productService.addProduct(request));
    }

    @GetMapping("/getAllProducts")
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @DeleteMapping("/deleteProduct/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}