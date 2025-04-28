package com.phucx.phucxfandb.service.discountType.impl;

import com.phucx.phucxfandb.dto.request.RequestDiscountTypeDTO;
import com.phucx.phucxfandb.dto.response.DiscountTypeDTO;
import com.phucx.phucxfandb.entity.DiscountType;
import com.phucx.phucxfandb.exception.EntityExistsException;
import com.phucx.phucxfandb.exception.NotFoundException;
import com.phucx.phucxfandb.mapper.DiscountTypeMapper;
import com.phucx.phucxfandb.repository.DiscountTypeRepository;
import com.phucx.phucxfandb.service.discountType.DiscountTypeUpdateService;
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
public class DiscountTypeUpdateServiceImpl implements DiscountTypeUpdateService {
    private final DiscountTypeRepository discountTypeRepository;
    private final DiscountTypeMapper mapper;

    @Override
    @Modifying
    @Transactional
    public DiscountTypeDTO updateDiscountType(Long typeId, RequestDiscountTypeDTO requestDiscountTypeDTO) {
        log.info("updateDiscountType(id={}, requestDiscountTypeDTO={})", typeId, requestDiscountTypeDTO);
        DiscountType existingDiscountType = discountTypeRepository.findByDiscountTypeIdAndIsDeletedFalse(typeId)
                .orElseThrow(() -> new NotFoundException("DiscountType", typeId));
        mapper.updateDiscountType(requestDiscountTypeDTO, existingDiscountType);
        // Save the updated discount type
        DiscountType updatedDiscountType = discountTypeRepository.save(existingDiscountType);
        return mapper.toDiscountTypeDTO(updatedDiscountType);
    }

    @Override
    @Modifying
    @Transactional
    public DiscountTypeDTO createDiscountType(RequestDiscountTypeDTO createDiscountTypeDTO) {
        log.info("createDiscountType(createDiscountTypeDTO={})", createDiscountTypeDTO);
        if (discountTypeRepository.existsByDiscountType(createDiscountTypeDTO.getDiscountType())) {
            throw new EntityExistsException("DiscountType " + createDiscountTypeDTO.getDiscountType() + " already exists");
        }
        DiscountType discountType = mapper.toDiscountType(createDiscountTypeDTO);
        DiscountType savedDiscountType = discountTypeRepository.save(discountType);
        return mapper.toDiscountTypeDTO(savedDiscountType);
    }

    @Override
    @Modifying
    @Transactional
    public List<DiscountTypeDTO> createDiscountTypes(List<RequestDiscountTypeDTO> createDiscountTypeDTOs) {
        log.info("createDiscountTypes(createDiscountTypeDTOs={})", createDiscountTypeDTOs);

        List<DiscountType> discountTypesToSave = createDiscountTypeDTOs.stream()
                .map(mapper::toDiscountType)
                .collect(Collectors.toList());

        return discountTypeRepository.saveAll(discountTypesToSave).stream()
                .map(mapper::toDiscountTypeDTO)
                .collect(Collectors.toList());
    }
}