package org.whiletrue.ordermanagementsystem.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.whiletrue.ordermanagementsystem.Domain.Entity.Order;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserId(Long userId);
}