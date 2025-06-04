package com.phucx.phucxfandb.service.refund;

public interface RefundProcessorService<T> {
    void processRefund(T entity);
}
