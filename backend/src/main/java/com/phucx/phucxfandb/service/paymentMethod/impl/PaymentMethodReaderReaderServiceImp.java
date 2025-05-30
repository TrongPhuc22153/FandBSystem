package com.phucx.phucxfandb.service.paymentMethod.impl;

import com.phucx.phucxfandb.dto.response.PaymentMethodDTO;
import com.phucx.phucxfandb.entity.PaymentMethod;
import com.phucx.phucxfandb.exception.NotFoundException;
import com.phucx.phucxfandb.mapper.PaymentMethodMapper;
import com.phucx.phucxfandb.repository.PaymentMethodRepository;
import com.phucx.phucxfandb.service.paymentMethod.PaymentMethodReaderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentMethodReaderReaderServiceImp implements PaymentMethodReaderService {
    private final PaymentMethodRepository paymentMethodRepository;
    private final PaymentMethodMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public List<PaymentMethodDTO> getPaymentMethods(String type) {
        return paymentMethodRepository.findByTypesName(type)
                .stream().map(mapper::toPaymentMethodDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentMethodDTO getPaymentMethod(String id) {
        return paymentMethodRepository.findById(id)
                .map(mapper::toPaymentMethodDTO)
                .orElseThrow(() -> new NotFoundException(PaymentMethod.class.getSimpleName(), "id", id));
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentMethod getPaymentMethodEntityByName(String methodName) {
        return paymentMethodRepository.findByMethodName(methodName)
                .orElseThrow(() -> new NotFoundException(PaymentMethod.class.getSimpleName(), "name", methodName));
    }
}
