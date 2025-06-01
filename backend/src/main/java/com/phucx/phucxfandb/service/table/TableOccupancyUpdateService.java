package com.phucx.phucxfandb.service.table;

import com.phucx.phucxfandb.enums.TableOccupancyStatus;
import com.phucx.phucxfandb.dto.request.RequestTableOccupancyDTO;
import com.phucx.phucxfandb.dto.response.TableOccupancyDTO;
import org.springframework.security.core.Authentication;

public interface TableOccupancyUpdateService {
    TableOccupancyDTO createTableOccupancy(Authentication authentication, RequestTableOccupancyDTO createRequest);

    TableOccupancyDTO updateTableOccupancyStatus(String id, RequestTableOccupancyDTO requestTableOccupancyDTO);
    TableOccupancyDTO updateTableOccupancy(String id, RequestTableOccupancyDTO updateRequest);
    void updateTableOccupancyStatus(String occupancyId, String tableId, TableOccupancyStatus status);
}
