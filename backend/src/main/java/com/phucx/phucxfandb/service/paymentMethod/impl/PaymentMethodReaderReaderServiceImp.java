package com.phucx.phucxfandb.service.paymentMethod.impl;

import com.phucx.phucxfandb.dto.response.PaymentMethodDTO;
import com.phucx.phucxfandb.entity.PaymentMethod;
import com.phucx.phucxfandb.exception.NotFoundException;
import com.phucx.phucxfandb.mapper.PaymentMethodMapper;
import com.phucx.phucxfandb.repository.PaymentMethodRepository;
import com.phucx.phucxfandb.service.paymentMethod.PaymentMethodReaderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentMethodReaderReaderServiceImp implements PaymentMethodReaderService {
    private final PaymentMethodRepository paymentMethodRepository;
    private final PaymentMethodMapper mapper;

    @Override
    public List<PaymentMethodDTO> getPaymentMethods() {
        return paymentMethodRepository.findAll()
                .stream().map(mapper::toPaymentMethodDTO)
                .collect(Collectors.toList());
    }

    @Override
    public PaymentMethod getPaymentMethodEntity(String id) {
        return paymentMethodRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(PaymentMethod.class.getSimpleName(), "id", id));
    }

    @Override
    public PaymentMethodDTO getPaymentMethod(String id) {
        return paymentMethodRepository.findById(id)
                .map(mapper::toPaymentMethodDTO)
                .orElseThrow(() -> new NotFoundException(PaymentMethod.class.getSimpleName(), "id", id));
    }
}
