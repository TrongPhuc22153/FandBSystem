package com.phucx.phucxfandb.service.table;

import com.phucx.phucxfandb.dto.request.RequestTableDTO;
import com.phucx.phucxfandb.dto.response.TableDTO;
import java.util.List;

public interface TableUpdateService {

    TableDTO updateTableStatus(String tableId, RequestTableDTO requestTableDTO);
    TableDTO updateTable(String tableId, RequestTableDTO requestTableDTO);

    TableDTO createTable(RequestTableDTO createTableDTO);
    List<TableDTO> createTables(List<RequestTableDTO> createTableDTOs);
}