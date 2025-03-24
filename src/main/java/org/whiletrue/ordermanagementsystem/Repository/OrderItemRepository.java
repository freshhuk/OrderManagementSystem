package org.whiletrue.ordermanagementsystem.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.whiletrue.ordermanagementsystem.Domain.Entity.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}