package org.whiletrue.ordermanagementsystem.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.whiletrue.ordermanagementsystem.Domain.Entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
}