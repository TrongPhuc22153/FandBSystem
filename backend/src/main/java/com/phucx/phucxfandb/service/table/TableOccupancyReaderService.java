package com.phucx.phucxfandb.service.table;

import com.phucx.phucxfandb.entity.TableOccupancy;
import com.phucx.phucxfandb.enums.TableOccupancyStatus;
import com.phucx.phucxfandb.dto.request.TableOccupancyRequestParamsDTO;
import com.phucx.phucxfandb.dto.response.TableOccupancyDTO;
import org.springframework.data.domain.Page;

public interface TableOccupancyReaderService {
    TableOccupancyDTO getTableOccupancy(String id);

    Page<TableOccupancyDTO> getTableOccupancies(TableOccupancyRequestParamsDTO params);

    TableOccupancy getTableOccupancyEntity(String id);

    boolean existsByTableIdAndTableOccupancyStatus(String tableId, TableOccupancyStatus status);
}
