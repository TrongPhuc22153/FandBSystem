package com.phucx.phucxfandb.mapper;

import com.phucx.phucxfandb.dto.request.RequestReservationTableDTO;
import com.phucx.phucxfandb.dto.response.ReservationTableDTO;
import com.phucx.phucxfandb.entity.ReservationTable;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ReservationTableMapper {
    ReservationTableDTO toReservationTableDTO(ReservationTable table);

    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "lastModifiedAt", ignore = true)
    @Mapping(target = "isDeleted", constant = "false")
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "tableId", ignore = true)
    ReservationTable toReservationTable(RequestReservationTableDTO requestReservationTableDTO);

    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "lastModifiedAt", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "tableId", ignore = true)
    void updateReservationTable(RequestReservationTableDTO requestReservationTableDTO, @MappingTarget ReservationTable table);


}
