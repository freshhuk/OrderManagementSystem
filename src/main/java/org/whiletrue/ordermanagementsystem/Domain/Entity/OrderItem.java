package org.whiletrue.ordermanagementsystem.Domain.Entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    private Order order;

    private int quantity;
}