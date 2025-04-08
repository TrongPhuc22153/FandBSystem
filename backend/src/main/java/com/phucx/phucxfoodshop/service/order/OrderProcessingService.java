package com.phucx.phucxfoodshop.service.order;

import com.phucx.phucxfoodshop.model.dto.OrderWithProducts;

public interface OrderProcessingService {
    public void processingOrder(OrderWithProducts customerorder);
}
