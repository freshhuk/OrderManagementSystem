package org.whiletrue.ordermanagementsystem.Services;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.whiletrue.ordermanagementsystem.Domain.Entity.*;
import org.whiletrue.ordermanagementsystem.Domain.Enums.OrderStatus;
import org.whiletrue.ordermanagementsystem.Domain.Models.CreateOrderRequest;
import org.whiletrue.ordermanagementsystem.Domain.Models.OrderItemRequest;
import org.whiletrue.ordermanagementsystem.Repository.OrderRepository;
import org.whiletrue.ordermanagementsystem.Repository.ProductRepository;
import org.whiletrue.ordermanagementsystem.Repository.UserRepository;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createOrder_success() {
        Long userId = 1L;
        Long productId = 10L;
        int quantity = 2;

        User mockUser = User.builder().id(userId).build();
        Product mockProduct = Product.builder().id(productId).build();

        OrderItemRequest itemRequest = new OrderItemRequest(productId, quantity);
        CreateOrderRequest request = new CreateOrderRequest(userId, List.of(itemRequest));

        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(productRepository.findById(productId)).thenReturn(Optional.of(mockProduct));
        when(orderRepository.save(any(Order.class))).thenAnswer(i -> {
            Order o = i.getArgument(0);
            o.setId(100L); // mock generated ID
            return o;
        });

        Order result = orderService.createOrder(request);

        assertNotNull(result);
        assertEquals(OrderStatus.CREATED, result.getStatus());
        assertEquals(1, result.getItems().size());
        assertEquals(mockUser, result.getUser());
        assertEquals(100L, result.getId());
    }

    @Test
    void createOrder_userNotFound() {
        Long userId = 99L;
        CreateOrderRequest request = new CreateOrderRequest(userId, new ArrayList<>());

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> orderService.createOrder(request));
    }

    @Test
    void createOrder_productNotFound() {
        Long userId = 1L;
        Long productId = 10L;

        User mockUser = User.builder().id(userId).build();
        OrderItemRequest itemRequest = new OrderItemRequest(productId, 1);
        CreateOrderRequest request = new CreateOrderRequest(userId, List.of(itemRequest));

        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> orderService.createOrder(request));
    }

    @Test
    void getOrder_found() {
        Long orderId = 1L;
        Order mockOrder = Order.builder().id(orderId).build();

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(mockOrder));

        Order result = orderService.getOrder(orderId);
        assertNotNull(result);
        assertEquals(orderId, result.getId());
    }

    @Test
    void getOrder_notFound() {
        Long orderId = 404L;
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> orderService.getOrder(orderId));
    }

    @Test
    void cancelOrder_success() {
        Long orderId = 1L;
        Order mockOrder = Order.builder().id(orderId).status(OrderStatus.CREATED).build();

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(mockOrder));
        when(orderRepository.save(any(Order.class))).thenReturn(mockOrder);

        orderService.cancelOrder(orderId);

        assertEquals(OrderStatus.CANCELLED, mockOrder.getStatus());
        verify(orderRepository).save(mockOrder);
    }

    @Test
    void getOrdersByUser_returnsList() {
        Long userId = 1L;
        List<Order> mockOrders = List.of(
                Order.builder().id(1L).build(),
                Order.builder().id(2L).build()
        );

        when(orderRepository.findByUserId(userId)).thenReturn(mockOrders);

        List<Order> result = orderService.getOrdersByUser(userId);

        assertEquals(2, result.size());
        verify(orderRepository).findByUserId(userId);
    }
}