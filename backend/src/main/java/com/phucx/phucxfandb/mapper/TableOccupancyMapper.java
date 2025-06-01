package com.phucx.phucxfandb.mapper;

import com.phucx.phucxfandb.dto.request.RequestTableOccupancyDTO;
import com.phucx.phucxfandb.dto.response.TableOccupancyDTO;
import com.phucx.phucxfandb.entity.TableOccupancy;
import com.phucx.phucxfandb.entity.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(componentModel = "spring", uses = OrderMapper.class)
public interface TableOccupancyMapper {

    @Named("toTableOccupancyDTO")
    @Mapping(target = "order", ignore = true)
    TableOccupancyDTO toTableOccupancyDTO(TableOccupancy tableOccupancy);

    @Mapping(target = "order", qualifiedByName = "toOrderListEntryDTO")
    TableOccupancyDTO toTableOccupancyDetailDTO(TableOccupancy tableOccupancy);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "employee", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "lastModifiedAt", ignore = true)
    void updateTableOccupancy(RequestTableOccupancyDTO requestDTO, @MappingTarget TableOccupancy tableOccupancy);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "employee", source = "employee")
    @Mapping(target = "notes", source = "requestDTO.notes")
    @Mapping(target = "type", source = "requestDTO.type")
    TableOccupancy toTableOccupancy(RequestTableOccupancyDTO requestDTO, Employee employee);
}
