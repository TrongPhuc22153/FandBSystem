package com.phucx.phucxfandb.service.discount.imp;

import com.phucx.phucxfandb.dto.request.RequestDiscountDTO;
import com.phucx.phucxfandb.dto.response.DiscountDTO;
import com.phucx.phucxfandb.entity.Discount;
import com.phucx.phucxfandb.entity.DiscountType;
import com.phucx.phucxfandb.entity.Product;
import com.phucx.phucxfandb.exception.NotFoundException;
import com.phucx.phucxfandb.mapper.DiscountMapper;
import com.phucx.phucxfandb.repository.DiscountRepository;
import com.phucx.phucxfandb.repository.DiscountTypeRepository;
import com.phucx.phucxfandb.service.discount.DiscountUpdateService;
import com.phucx.phucxfandb.service.product.ProductReaderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class DiscountUpdateServiceImpl implements DiscountUpdateService {
    private final ProductReaderService productReaderService;
    private final DiscountRepository discountRepository;
    private final DiscountTypeRepository discountTypeRepository;
    private final DiscountMapper mapper;

    @Override
    @Modifying
    @Transactional
    public DiscountDTO createDiscount(RequestDiscountDTO requestDiscountDTO) {
        log.info("createDiscount(requestDiscountDTO={})", requestDiscountDTO);
        Product product = productReaderService.getProductEntity(requestDiscountDTO.getProductId());
        DiscountType discountType = discountTypeRepository.findById(requestDiscountDTO.getDiscountTypeId())
                .orElseThrow(() -> new NotFoundException("Discount type", requestDiscountDTO.getDiscountTypeId()));

        Discount newDiscount = mapper.toDiscount(requestDiscountDTO, discountType);
        newDiscount.getProducts().add(product);
        Discount savedDiscount = discountRepository.save(newDiscount);

        return mapper.toDiscountDTO(savedDiscount);
    }

    @Override
    @Modifying
    @Transactional
    public DiscountDTO updateDiscount(String discountId, RequestDiscountDTO requestDiscountDTO) {
        log.info("updateDiscount(discountId={}, requestDiscountDTO={})", discountId, requestDiscountDTO);
        Discount existingDiscount = discountRepository.findById(discountId)
                .orElseThrow(()-> new NotFoundException("Discount", discountId));
        DiscountType discountType = discountTypeRepository.findById(requestDiscountDTO.getDiscountTypeId())
                .orElseThrow(() -> new NotFoundException("Discount type", requestDiscountDTO.getDiscountTypeId()));

        mapper.updateDiscount(requestDiscountDTO, discountType, existingDiscount);
        Discount updatedDiscount = discountRepository.save(existingDiscount);
        return mapper.toDiscountDTO(updatedDiscount);
    }
}
