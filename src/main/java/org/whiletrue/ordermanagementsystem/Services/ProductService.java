package org.whiletrue.ordermanagementsystem.Services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.whiletrue.ordermanagementsystem.Domain.Entity.Product;
import org.whiletrue.ordermanagementsystem.Domain.Models.CreateProductRequest;
import org.whiletrue.ordermanagementsystem.Repository.ProductRepository;

import java.util.List;

/**
 * Service responsible for managing product-related operations such as
 * adding, retrieving, and deleting products.
 */
@Service
public class ProductService {

    private final ProductRepository productRepository;
    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /**
     * Adds a new product to the database.
     *
     * @param request The request containing product name and price.
     * @return The saved Product object.
     */
    public Product addProduct(CreateProductRequest request) {
        logger.info("Adding new product: name='{}', price={}", request.name(), request.price());

        Product product = Product.builder()
                .name(request.name())
                .price(request.price())
                .build();

        Product savedProduct = productRepository.save(product);
        logger.info("Product added successfully with ID: {}", savedProduct.getId());

        return savedProduct;
    }

    /**
     * Retrieves all products available in the system.
     *
     * @return List of all Product objects.
     */
    public List<Product> getAllProducts() {
        logger.info("Fetching all products");
        List<Product> products = productRepository.findAll();
        logger.info("Retrieved {} product(s)", products.size());
        return products;
    }

    /**
     * Deletes a product by its ID.
     *
     * @param id The ID of the product to delete.
     */
    public void deleteProduct(Long id) {
        logger.info("Deleting product with ID: {}", id);
        productRepository.deleteById(id);
        logger.info("Product with ID {} has been deleted", id);
    }
}
