package com.phucx.phucxfoodshop.service.order;

import com.phucx.phucxfoodshop.model.OrderWithProducts;

public interface OrderProcessingService {
    public void processingOrder(OrderWithProducts customerorder);
}
