package com.phucx.phucxfandb.service.discount.impl;

import com.phucx.phucxfandb.dto.request.DiscountRequestParamsDTO;
import com.phucx.phucxfandb.dto.response.DiscountDTO;
import com.phucx.phucxfandb.entity.Discount;
import com.phucx.phucxfandb.exception.NotFoundException;
import com.phucx.phucxfandb.mapper.DiscountMapper;
import com.phucx.phucxfandb.repository.DiscountRepository;
import com.phucx.phucxfandb.service.discount.DiscountReaderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class DiscountReaderServiceImp implements DiscountReaderService {
    private final DiscountRepository discountRepository;
    private final DiscountMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public Page<DiscountDTO> getDiscountsByProductId(long productId, DiscountRequestParamsDTO params) {
        Pageable pageable = PageRequest.of(params.getPage(), params.getSize(), Sort.by(params.getDirection(), params.getField()));
        return discountRepository.findByProductsProductIdAndIsDeletedFalse(productId, pageable)
                .map(mapper::toDiscountDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public DiscountDTO getDiscount(String discountID) {
        Discount discount = discountRepository.findById(discountID)
                .orElseThrow(() -> new NotFoundException("Discount", discountID));
        return mapper.toDiscountDTO(discount);
    }
}
