package com.phucx.phucxfandb.service.discount.imp;

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
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class DiscountReaderServiceImp implements DiscountReaderService {
    private final DiscountRepository discountRepository;
    private final DiscountMapper mapper;

    @Override
    public Page<DiscountDTO> getDiscounts(int pageNumber, int pageSize) {
        log.info("getDiscounts(pageNumber={}, pageSize={})", pageNumber, pageSize);
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return discountRepository.findByIsDeletedFalse(pageable)
                .map(mapper::toDiscountDTO);
    }

    @Override
    public Page<DiscountDTO> getDiscountsByProductId(long productId, int pageNumber, int pageSize) {
        log.info("getDiscountsByProductId(productId={}, pageNumber={}, pageSize={})", productId, pageNumber, pageSize);
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return discountRepository.findByProductsProductIdAndIsDeletedFalse(productId, pageable)
                .map(mapper::toDiscountDTO);
    }

    @Override
    public DiscountDTO getDiscount(String discountID) {
        log.info("getDiscount(discountID={})", discountID);
        Discount discount = discountRepository.findById(discountID)
                .orElseThrow(() -> new NotFoundException("Discount", discountID));
        return mapper.toDiscountDTO(discount);
    }
}
