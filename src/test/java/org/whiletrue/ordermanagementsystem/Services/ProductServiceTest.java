package org.whiletrue.ordermanagementsystem.Services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.whiletrue.ordermanagementsystem.Domain.Entity.Product;
import org.whiletrue.ordermanagementsystem.Domain.Models.CreateProductRequest;
import org.whiletrue.ordermanagementsystem.Repository.ProductRepository;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addProduct_success() {
        // Arrange
        CreateProductRequest request = new CreateProductRequest("Test Product", new BigDecimal("49.99"));
        Product mockProduct = Product.builder()
                .id(1L)
                .name("Test Product")
                .price(new BigDecimal("49.99"))
                .build();

        when(productRepository.save(any(Product.class))).thenReturn(mockProduct);

        // Act
        Product result = productService.addProduct(request);

        // Assert
        assertNotNull(result);
        assertEquals("Test Product", result.getName());
        assertEquals(new BigDecimal("49.99"), result.getPrice());
        assertEquals(1L, result.getId());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void getAllProducts_returnsList() {
        // Arrange
        List<Product> mockList = List.of(
                Product.builder().id(1L).name("A").price(new BigDecimal("10.00")).build(),
                Product.builder().id(2L).name("B").price(new BigDecimal("20.00")).build()
        );

        when(productRepository.findAll()).thenReturn(mockList);

        // Act
        List<Product> result = productService.getAllProducts();

        // Assert
        assertEquals(2, result.size());
        assertEquals("A", result.get(0).getName());
        assertEquals("B", result.get(1).getName());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void deleteProduct_success() {
        // Arrange
        Long productId = 1L;

        // Act
        productService.deleteProduct(productId);

        // Assert
        verify(productRepository, times(1)).deleteById(productId);
    }
}