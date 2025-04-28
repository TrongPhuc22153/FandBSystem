package com.phucx.phucxfandb.service.discountType.impl;

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
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class DiscountTypeReaderServiceImp implements DiscountTypeReaderService {
    private final DiscountTypeRepository discountTypeRepository;
    private final DiscountTypeMapper mapper;


    @Override
    public Page<DiscountTypeDTO> getDiscountTypes(int pageNumber, int pageSize){
        log.info("getDiscountTypes(pageNumber={}, pageSize={})", pageNumber, pageSize);
        Pageable page = PageRequest.of(pageNumber, pageSize);
        return discountTypeRepository.findByIsDeletedFalse(page)
                .map(mapper::toDiscountTypeDTO);

    }

    @Override
    public DiscountTypeDTO getDiscountType(long typeId){
        log.info("getDiscountType(typeId={})", typeId);
        DiscountType discountType = discountTypeRepository.findByDiscountTypeIdAndIsDeletedFalse(typeId)
                .orElseThrow(()-> new NotFoundException("DiscountType", typeId));
        return mapper.toDiscountTypeDTO(discountType);
    }


    @Override
    public DiscountTypeDTO getDiscountType(String typeName){
        log.info("getDiscountType(typeName={})", typeName);
        DiscountType discountType = discountTypeRepository.findByDiscountTypeAndIsDeletedFalse(typeName)
                .orElseThrow(()-> new NotFoundException("DiscountType", typeName));
        return mapper.toDiscountTypeDTO(discountType);
    }

    @Override
    public DiscountType getDiscountTypeEntity(long typeId) {
        log.info("getDiscountTypeEntity(typeId={})", typeId);
        return discountTypeRepository.findByDiscountTypeIdAndIsDeletedFalse(typeId)
                .orElseThrow(()-> new NotFoundException("DiscountType", typeId));
    }
}
