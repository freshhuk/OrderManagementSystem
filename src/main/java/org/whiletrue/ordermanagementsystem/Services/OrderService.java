package org.whiletrue.ordermanagementsystem.Services;

import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.whiletrue.ordermanagementsystem.Domain.Entity.Order;
import org.whiletrue.ordermanagementsystem.Domain.Entity.OrderItem;
import org.whiletrue.ordermanagementsystem.Domain.Entity.Product;
import org.whiletrue.ordermanagementsystem.Domain.Entity.User;
import org.whiletrue.ordermanagementsystem.Domain.Enums.OrderStatus;
import org.whiletrue.ordermanagementsystem.Domain.Models.CreateOrderRequest;
import org.whiletrue.ordermanagementsystem.Domain.Models.OrderItemRequest;
import org.whiletrue.ordermanagementsystem.Repository.OrderRepository;
import org.whiletrue.ordermanagementsystem.Repository.ProductRepository;
import org.whiletrue.ordermanagementsystem.Repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Service responsible for order management operations such as creation, retrieval,
 * cancellation, and querying by user.
 */
@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    @Autowired
    public OrderService(OrderRepository orderRepository, UserRepository userRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    /**
     * Creates a new order based on the request details.
     *
     * @param request The order creation request including user ID and order items.
     * @return The created Order object.
     * @throws EntityNotFoundException if the user or any product is not found.
     */
    public Order createOrder(CreateOrderRequest request) {
        logger.info("Creating order for user with ID: {}", request.userId());

        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> {
                    logger.error("User not found with ID: {}", request.userId());
                    return new EntityNotFoundException("User not found");
                });

        List<OrderItem> items = new ArrayList<>();
        Order order = Order.builder()
                .user(user)
                .status(OrderStatus.CREATED)
                .createdAt(LocalDateTime.now())
                .build();

        for (OrderItemRequest itemReq : request.items()) {
            Product product = productRepository.findById(itemReq.productId())
                    .orElseThrow(() -> {
                        logger.error("Product not found with ID: {}", itemReq.productId());
                        return new EntityNotFoundException("Product not found");
                    });

            OrderItem item = OrderItem.builder()
                    .product(product)
                    .quantity(itemReq.quantity())
                    .order(order)
                    .build();

            items.add(item);
            logger.debug("Added item: productId={}, quantity={}", itemReq.productId(), itemReq.quantity());
        }

        order.setItems(items);
        Order savedOrder = orderRepository.save(order);

        logger.info("Order created successfully with ID: {}", savedOrder.getId());
        return savedOrder;
    }

    /**
     * Retrieves an order by its ID.
     *
     * @param id The order ID.
     * @return The Order object.
     * @throws EntityNotFoundException if the order is not found.
     */
    public Order getOrder(Long id) {
        logger.info("Fetching order with ID: {}", id);
        return orderRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Order not found with ID: {}", id);
                    return new EntityNotFoundException("Order not found");
                });
    }

    /**
     * Cancels an existing order by updating its status.
     *
     * @param id The order ID.
     */
    public void cancelOrder(Long id) {
        logger.info("Cancelling order with ID: {}", id);
        Order order = getOrder(id);
        order.setStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);
        logger.info("Order with ID {} has been cancelled", id);
    }

    /**
     * Retrieves all orders placed by a specific user.
     *
     * @param userId The user ID.
     * @return A list of Order objects.
     */
    public List<Order> getOrdersByUser(Long userId) {
        logger.info("Fetching orders for user with ID: {}", userId);
        return orderRepository.findByUserId(userId);
    }
}
