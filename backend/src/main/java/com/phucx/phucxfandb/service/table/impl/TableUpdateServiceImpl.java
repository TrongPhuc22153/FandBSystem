package com.phucx.phucxfandb.service.table.impl;

import com.phucx.phucxfandb.entity.TableEntity;
import com.phucx.phucxfandb.dto.request.RequestTableDTO;
import com.phucx.phucxfandb.dto.response.TableDTO;
import com.phucx.phucxfandb.exception.EntityExistsException;
import com.phucx.phucxfandb.exception.NotFoundException;
import com.phucx.phucxfandb.mapper.TableMapper;
import com.phucx.phucxfandb.repository.TableRepository;
import com.phucx.phucxfandb.service.table.TableUpdateService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TableUpdateServiceImpl implements TableUpdateService {
    private final TableRepository tableRepository;
    private final TableMapper mapper;

    @Override
    @Transactional
    public TableDTO updateTableStatus(String tableId, RequestTableDTO requestTableDTO) {
        TableEntity existingTableEntity = tableRepository.findById(tableId)
                .orElseThrow(() -> new NotFoundException(TableEntity.class.getSimpleName(), "id", tableId));
        existingTableEntity.setIsDeleted(requestTableDTO.getIsDeleted());
        TableEntity updatedTableEntity = tableRepository.save(existingTableEntity);
        return mapper.toTableDTO(updatedTableEntity);
    }

    @Override
    @Transactional
    public TableDTO updateTable(String tableId, RequestTableDTO requestTableDTO) {
        TableEntity existingTableEntity = tableRepository.findByTableIdAndIsDeletedFalse(tableId)
                .orElseThrow(() -> new NotFoundException(TableEntity.class.getSimpleName(), tableId));
        mapper.updateTable(requestTableDTO, existingTableEntity);
        TableEntity updatedTableEntity = tableRepository.save(existingTableEntity);
        return mapper.toTableDTO(updatedTableEntity);
    }

    @Override
    @Transactional
    public TableDTO createTable(RequestTableDTO createReservationTableDTO) {
        if (tableRepository.existsByTableNumber(createReservationTableDTO.getTableNumber())) {
            throw new EntityExistsException("TableEntity " + createReservationTableDTO.getTableNumber() + " already exists");
        }
        TableEntity tableEntity = mapper.toTable(createReservationTableDTO);
        TableEntity savedTableEntity = tableRepository.save(tableEntity);
        return mapper.toTableDTO(savedTableEntity);
    }

    @Override
    @Transactional
    public List<TableDTO> createTables(List<RequestTableDTO> createReservationTableDTOs) {
        List<TableEntity> tablesToSaveEntity = createReservationTableDTOs.stream()
                .map(mapper::toTable)
                .collect(Collectors.toList());
        return tableRepository.saveAll(tablesToSaveEntity).stream()
                .map(mapper::toTableDTO)
                .collect(Collectors.toList());
    }
}