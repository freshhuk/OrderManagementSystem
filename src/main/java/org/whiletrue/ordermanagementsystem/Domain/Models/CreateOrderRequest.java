package org.whiletrue.ordermanagementsystem.Domain.Models;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CreateOrderRequest(
        @NotNull Long userId,
        @NotEmpty List<OrderItemRequest> items
) {}
