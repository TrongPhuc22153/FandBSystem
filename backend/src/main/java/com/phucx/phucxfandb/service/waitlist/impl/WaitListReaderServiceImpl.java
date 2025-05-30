package com.phucx.phucxfandb.service.waitlist.impl;

import com.phucx.phucxfandb.enums.WaitListStatus;
import com.phucx.phucxfandb.dto.request.WaitListRequestParamsDTO;
import com.phucx.phucxfandb.dto.response.WaitListDTO;
import com.phucx.phucxfandb.entity.WaitList;
import com.phucx.phucxfandb.exception.NotFoundException;
import com.phucx.phucxfandb.mapper.WaitListMapper;
import com.phucx.phucxfandb.repository.WaitListRepository;
import com.phucx.phucxfandb.service.waitlist.WaitListReaderService;
import com.phucx.phucxfandb.specifications.WaitListSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class WaitListReaderServiceImpl implements WaitListReaderService {
    private final WaitListRepository waitListRepository;
    private final WaitListMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public WaitListDTO getWaitList(String id) {
        return waitListRepository.findById(id)
                .map(mapper::toWaitListDetailDTO)
                .orElseThrow(() -> new NotFoundException(WaitList.class.getSimpleName(), "id", id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<WaitListDTO> getWaitLists(WaitListRequestParamsDTO params) {
        Pageable pageable = PageRequest.of(params.getPage(), params.getSize(), Sort.by(params.getDirection(), params.getField()));

        Specification<WaitList> spec = Specification
                .where(WaitListSpecification.hasStatus(params.getStatus()));

        return waitListRepository.findAll(spec, pageable)
                .map(mapper::toWaitListDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public WaitList getWaitListEntity(String id) {
        return waitListRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(WaitList.class.getSimpleName(), "id", id));
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByTableIdAndWaitListStatus(String tableId, WaitListStatus status) {
        return waitListRepository.existsByTableIdAndWaitListStatus(tableId, status);
    }
}
