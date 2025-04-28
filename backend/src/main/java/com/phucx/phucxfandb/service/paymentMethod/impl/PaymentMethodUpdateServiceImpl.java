package com.phucx.phucxfandb.service.paymentMethod.impl;

import com.phucx.phucxfandb.dto.request.RequestPaymentMethodDTO;
import com.phucx.phucxfandb.dto.response.PaymentMethodDTO;
import com.phucx.phucxfandb.entity.PaymentMethod;
import com.phucx.phucxfandb.exception.EntityExistsException;
import com.phucx.phucxfandb.exception.NotFoundException;
import com.phucx.phucxfandb.mapper.PaymentMethodMapper;
import com.phucx.phucxfandb.repository.PaymentMethodRepository;
import com.phucx.phucxfandb.service.paymentMethod.PaymentMethodUpdateService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentMethodUpdateServiceImpl implements PaymentMethodUpdateService {
    private final PaymentMethodRepository paymentMethodRepository;
    private final PaymentMethodMapper mapper;


    @Override
    @Modifying
    @Transactional
    public PaymentMethodDTO updatePaymentMethod(String methodId, RequestPaymentMethodDTO requestPaymentMethodDTO) {
        log.info("updatePaymentMethod(methodId={}, requestPaymentMethodDTO={})", methodId, requestPaymentMethodDTO);
        PaymentMethod existingPaymentMethod = paymentMethodRepository.findById(methodId)
                .orElseThrow(() -> new NotFoundException("PaymentMethod", methodId));
        mapper.updatePaymentMethodFromDTO(requestPaymentMethodDTO, existingPaymentMethod);
        // Save the updated payment method
        PaymentMethod updatedPaymentMethod = paymentMethodRepository.save(existingPaymentMethod);
        return mapper.toPaymentMethodDTO(updatedPaymentMethod);
    }

    @Override
    @Modifying
    @Transactional
    public PaymentMethodDTO createPaymentMethod(RequestPaymentMethodDTO createPaymentMethodDTO) {
        log.info("createPaymentMethod(createPaymentMethodDTO={})", createPaymentMethodDTO);
        if (paymentMethodRepository.existsByMethodName(createPaymentMethodDTO.getMethodName())) {
            throw new EntityExistsException("Payment method with name " + createPaymentMethodDTO.getMethodName() + " already exists");
        }
        PaymentMethod paymentMethod = mapper.toPaymentMethod(createPaymentMethodDTO);
        PaymentMethod savedPaymentMethod = paymentMethodRepository.save(paymentMethod);
        return mapper.toPaymentMethodDTO(savedPaymentMethod);
    }

    @Override
    @Modifying
    @Transactional
    public List<PaymentMethodDTO> createPaymentMethods(List<RequestPaymentMethodDTO> createPaymentMethodDTOs) {
        log.info("createPaymentMethods(createPaymentMethodDTOs={})", createPaymentMethodDTOs);

        List<PaymentMethod> paymentMethodsToSave = createPaymentMethodDTOs.stream()
                .map(mapper::toPaymentMethod)
                .collect(Collectors.toList());

        return paymentMethodRepository.saveAll(paymentMethodsToSave).stream()
                .map(mapper::toPaymentMethodDTO)
                .collect(Collectors.toList());
    }
}
