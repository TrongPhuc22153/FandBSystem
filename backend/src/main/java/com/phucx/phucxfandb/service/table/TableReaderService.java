package com.phucx.phucxfandb.service.table;

import com.phucx.phucxfandb.dto.request.AvailableTableRequestParamsDTO;
import com.phucx.phucxfandb.dto.request.TableRequestParamsDTODTO;
import com.phucx.phucxfandb.dto.response.TableDTO;
import com.phucx.phucxfandb.entity.TableEntity;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface TableReaderService {
    Page<TableDTO> getTables(TableRequestParamsDTODTO params);

    Page<TableDTO> getTableAvailability(AvailableTableRequestParamsDTO params);

    TableDTO getTable(String tableId);

    TableEntity getTableEntity(String tableId);

    TableEntity getAvailableTable(int numberOfGuests, LocalDate date, LocalTime requestedStartTime, LocalTime requestedEndTime);

    List<TableEntity> getAvailableTables(LocalDate date, LocalTime time, int partySize, int durationMinutes);
}