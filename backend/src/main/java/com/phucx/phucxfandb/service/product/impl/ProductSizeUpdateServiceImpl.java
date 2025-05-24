package com.phucx.phucxfandb.service.product.impl;

import com.phucx.phucxfandb.dto.request.RequestProductSizeDTO;
import com.phucx.phucxfandb.dto.response.ProductSizeDTO;
import com.phucx.phucxfandb.entity.ProductSize;
import com.phucx.phucxfandb.exception.NotFoundException;
import com.phucx.phucxfandb.mapper.ProductSizeMapper;
import com.phucx.phucxfandb.repository.ProductSizeRepository;
import com.phucx.phucxfandb.service.product.ProductSizeUpdateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductSizeUpdateServiceImpl implements ProductSizeUpdateService {
    private final ProductSizeRepository productSizeRepository;
    private final ProductSizeMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public ProductSizeDTO updateProductSize(long productId, RequestProductSizeDTO requestProductSizeDTO) {
        log.info("updateProductSize(productId={}, requestProductSizeDTO={})", productId, requestProductSizeDTO);
        ProductSize productSize = productSizeRepository.findByProductProductId(productId)
                .orElseThrow(() -> new NotFoundException("ProductSize", "product", "id", String.valueOf(productId)));

        mapper.updateProductSizeFromDTO(requestProductSizeDTO, productSize);
        ProductSize updatedProductSize = productSizeRepository.save(productSize);
        return mapper.toProductSizeDTO(updatedProductSize);
    }
}
