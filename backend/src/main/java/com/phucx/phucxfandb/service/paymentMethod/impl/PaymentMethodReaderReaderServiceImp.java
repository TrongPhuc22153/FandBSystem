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
        log.info("getPaymentMethods()");
        return paymentMethodRepository.findAll()
                .stream().map(mapper::toPaymentMethodDTO)
                .collect(Collectors.toList());
    }

    @Override
    public PaymentMethod getPaymentMethodEntity(String id) {
        log.info("getPaymentMethodEntity(id={})", id);
        return paymentMethodRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("PaymentMethod", id));
    }

    @Override
    public Page<PaymentMethodDTO> getPaymentMethods(int pageNumber, int pageSize) {
        log.info("getPaymentMethods(pageNumber={}, pageSize={})", pageNumber, pageSize);
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return paymentMethodRepository.findAll(pageable)
                .map(mapper::toPaymentMethodDTO);
    }

    @Override
    public PaymentMethodDTO getPaymentMethod(String methodId) {
        log.info("getPaymentMethod(methodId={})", methodId);
        PaymentMethod paymentMethod = paymentMethodRepository.findById(methodId)
                .orElseThrow(() -> new NotFoundException("PaymentMethod", methodId));
        return mapper.toPaymentMethodDTO(paymentMethod);
    }
}
