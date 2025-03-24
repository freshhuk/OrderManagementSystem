package org.whiletrue.ordermanagementsystem.Domain.Models;

import jakarta.validation.constraints.NotNull;

public record OrderItemRequest(
        @NotNull Long productId,
        int quantity
) {}
