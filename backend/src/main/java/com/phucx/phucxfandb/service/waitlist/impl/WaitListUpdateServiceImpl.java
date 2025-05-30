package com.phucx.phucxfandb.service.waitlist.impl;

import com.phucx.phucxfandb.enums.TableStatus;
import com.phucx.phucxfandb.enums.WaitListStatus;
import com.phucx.phucxfandb.dto.request.RequestWaitListDTO;
import com.phucx.phucxfandb.dto.response.WaitListDTO;
import com.phucx.phucxfandb.entity.ReservationTable;
import com.phucx.phucxfandb.entity.WaitList;
import com.phucx.phucxfandb.entity.Employee;
import com.phucx.phucxfandb.exception.NotFoundException;
import com.phucx.phucxfandb.exception.TableException;
import com.phucx.phucxfandb.mapper.WaitListMapper;
import com.phucx.phucxfandb.repository.WaitListRepository;
import com.phucx.phucxfandb.service.employee.EmployeeReaderService;
import com.phucx.phucxfandb.service.table.ReservationTableReaderService;
import com.phucx.phucxfandb.service.table.ReservationTableUpdateService;
import com.phucx.phucxfandb.service.waitlist.WaitListUpdateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class WaitListUpdateServiceImpl implements WaitListUpdateService {
    private final EmployeeReaderService employeeReaderService;
    private final ReservationTableReaderService tableReaderService;
    private final ReservationTableUpdateService tableUpdateService;
    private final WaitListRepository repository;
    private final WaitListMapper mapper;

    @Override
    @Transactional
    public WaitListDTO createWaitList(Authentication authentication, RequestWaitListDTO createRequest) {
        String username = authentication.getName();
        Employee employee = employeeReaderService.getEmployeeEntityByUsername(username);

        WaitList waitList = mapper.toWaitList(createRequest, employee);
        WaitList saved = repository.save(waitList);
        return mapper.toWaitListDTO(saved);
    }

    @Override
    @Transactional
    public WaitListDTO updateWaitListStatus(String id, RequestWaitListDTO requestWaitListDTO) {
        WaitList waitList = repository.findById(id)
                .orElseThrow(() -> new NotFoundException(WaitList.class.getSimpleName(), "id", id));
        if(requestWaitListDTO.getStatus().equals(WaitListStatus.SEATED)){
            String tableId = requestWaitListDTO.getTableId();
            if(tableId==null || tableId.isBlank()){
                throw new IllegalArgumentException("Missing table id");
            }
            ReservationTable table = tableReaderService.getReservationTableEntity(requestWaitListDTO.getTableId());
            if(!table.getStatus().equals(TableStatus.UNOCCUPIED)){
                throw new TableException(String.format("Table %d is not available", table.getTableNumber()));
            }
            tableUpdateService.updateTableStatus(table.getTableId(), TableStatus.OCCUPIED);
            waitList.setTable(table);
        }else if(requestWaitListDTO.getStatus().equals(WaitListStatus.COMPLETED)){
            String tableId = requestWaitListDTO.getTableId();
            if(tableId==null || tableId.isBlank()){
                throw new IllegalArgumentException("Missing table id");
            }
            ReservationTable table = tableReaderService.getReservationTableEntity(requestWaitListDTO.getTableId());
            tableUpdateService.updateTableStatus(table.getTableId(), TableStatus.CLEANING);
        }
        waitList.setStatus(requestWaitListDTO.getStatus());
        WaitList updated = repository.save(waitList);
        return mapper.toWaitListDTO(updated);
    }

    @Override
    @Transactional
    public WaitListDTO updateWaitList(String id, RequestWaitListDTO updateRequest) {
        WaitList waitList = repository.findById(id)
                .orElseThrow(() -> new NotFoundException(WaitList.class.getSimpleName(), "id", id));
        mapper.updateWaitList(updateRequest, waitList);
        WaitList updated = repository.save(waitList);
        return mapper.toWaitListDTO(updated);
    }

    @Override
    @Transactional
    public void updateWaitListStatus(String id, WaitListStatus status) {
        WaitList waitList = repository.findById(id)
                .orElseThrow(() -> new NotFoundException(WaitList.class.getSimpleName(), "id", id));
        String tableId = waitList.getTable().getTableId();
        if(status.equals(WaitListStatus.SEATED)){
            if(tableId==null || tableId.isBlank()){
                throw new IllegalArgumentException("Missing table id");
            }
            ReservationTable table = tableReaderService.getReservationTableEntity(tableId);
            if(!table.getStatus().equals(TableStatus.UNOCCUPIED)){
                throw new TableException(String.format("Table %d is not available", table.getTableNumber()));
            }
            tableUpdateService.updateTableStatus(table.getTableId(), TableStatus.OCCUPIED);
            waitList.setTable(table);
        }else if(status.equals(WaitListStatus.COMPLETED)){
            if(tableId==null || tableId.isBlank()){
                throw new IllegalArgumentException("Missing table id");
            }
            ReservationTable table = tableReaderService.getReservationTableEntity(tableId);
            tableUpdateService.updateTableStatus(table.getTableId(), TableStatus.CLEANING);
        }
        waitList.setStatus(status);
        repository.save(waitList);
    }
}
