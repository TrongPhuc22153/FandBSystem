package com.phucx.phucxfandb.service.refund;

import com.phucx.phucxfandb.dto.response.RefundPreviewDTO;
import com.phucx.phucxfandb.entity.Order;

public interface OrderRefundProcessorService extends RefundProcessorService<Order> {
    RefundPreviewDTO validateRefund(String orderId);
}
