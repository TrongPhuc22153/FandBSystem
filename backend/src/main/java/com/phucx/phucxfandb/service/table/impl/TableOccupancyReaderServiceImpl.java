package com.phucx.phucxfandb.service.table.impl;

import com.phucx.phucxfandb.enums.TableOccupancyStatus;
import com.phucx.phucxfandb.dto.request.TableOccupancyRequestParamsDTO;
import com.phucx.phucxfandb.dto.response.TableOccupancyDTO;
import com.phucx.phucxfandb.entity.TableOccupancy;
import com.phucx.phucxfandb.exception.NotFoundException;
import com.phucx.phucxfandb.mapper.TableOccupancyMapper;
import com.phucx.phucxfandb.repository.TableOccupancyRepository;
import com.phucx.phucxfandb.service.table.TableOccupancyReaderService;
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
public class TableOccupancyReaderServiceImpl implements TableOccupancyReaderService {
    private final TableOccupancyRepository tableOccupancyRepository;
    private final TableOccupancyMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public TableOccupancyDTO getTableOccupancy(String id) {
        return tableOccupancyRepository.findById(id)
                .map(mapper::toTableOccupancyDetailDTO)
                .orElseThrow(() -> new NotFoundException(TableOccupancy.class.getSimpleName(), "id", id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TableOccupancyDTO> getTableOccupancies(TableOccupancyRequestParamsDTO params) {
        Pageable pageable = PageRequest.of(params.getPage(), params.getSize(), Sort.by(params.getDirection(), params.getField()));

        Specification<TableOccupancy> spec = Specification
                .where(WaitListSpecification.hasStatus(params.getStatus()));

        return tableOccupancyRepository.findAll(spec, pageable)
                .map(mapper::toTableOccupancyDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public TableOccupancy getTableOccupancyEntity(String id) {
        return tableOccupancyRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(TableOccupancy.class.getSimpleName(), "id", id));
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByTableIdAndTableOccupancyStatus(String tableId, TableOccupancyStatus status) {
        return tableOccupancyRepository.existsByTableIdAndTableOccupancyStatus(tableId, status);
    }
}
