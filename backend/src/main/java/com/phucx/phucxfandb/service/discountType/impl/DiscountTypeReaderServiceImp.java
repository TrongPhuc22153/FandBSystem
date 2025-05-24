package com.phucx.phucxfandb.service.discountType.impl;

import com.phucx.phucxfandb.dto.request.DiscountTypeParamsDTO;
import com.phucx.phucxfandb.dto.response.DiscountTypeDTO;
import com.phucx.phucxfandb.entity.DiscountType;
import com.phucx.phucxfandb.exception.NotFoundException;
import com.phucx.phucxfandb.mapper.DiscountTypeMapper;
import com.phucx.phucxfandb.repository.DiscountTypeRepository;
import com.phucx.phucxfandb.service.discountType.DiscountTypeReaderService;
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
public class DiscountTypeReaderServiceImp implements DiscountTypeReaderService {
    private final DiscountTypeRepository discountTypeRepository;
    private final DiscountTypeMapper mapper;


    @Override
    @Transactional(readOnly = true)
    public Page<DiscountTypeDTO> getDiscountTypes(DiscountTypeParamsDTO params){
        Pageable page = PageRequest.of(params.getPage(), params.getSize(), Sort.by(params.getDirection(), params.getField()));
        return discountTypeRepository.findByIsDeletedFalse(page)
                .map(mapper::toDiscountTypeDTO);

    }

    @Override
    @Transactional(readOnly = true)
    public DiscountTypeDTO getDiscountType(long typeId){
        DiscountType discountType = discountTypeRepository.findByDiscountTypeIdAndIsDeletedFalse(typeId)
                .orElseThrow(()-> new NotFoundException(DiscountType.class.getSimpleName(), "id", typeId));
        return mapper.toDiscountTypeDTO(discountType);
    }

    @Override
    @Transactional(readOnly = true)
    public DiscountType getDiscountTypeEntity(long typeId) {
        return discountTypeRepository.findByDiscountTypeIdAndIsDeletedFalse(typeId)
                .orElseThrow(()-> new NotFoundException(DiscountType.class.getSimpleName(), "id", typeId));
    }
}
