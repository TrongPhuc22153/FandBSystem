package com.phucx.phucxfandb.mapper;

import com.phucx.phucxfandb.dto.request.RequestWaitListDTO;
import com.phucx.phucxfandb.dto.response.WaitListDTO;
import com.phucx.phucxfandb.entity.WaitList;
import com.phucx.phucxfandb.entity.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(componentModel = "spring", uses = OrderMapper.class)
public interface WaitListMapper {

    @Named("toWaitListDTO")
    @Mapping(target = "order", ignore = true)
    WaitListDTO toWaitListDTO(WaitList waitList);

    @Mapping(target = "order", qualifiedByName = "toOrderListEntryDTO")
    WaitListDTO toWaitListDetailDTO(WaitList waitList);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "employee", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "lastModifiedAt", ignore = true)
    void updateWaitList(RequestWaitListDTO requestDTO, @MappingTarget WaitList waitList);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "employee", source = "employee")
    @Mapping(target = "notes", source = "requestDTO.notes")
    WaitList toWaitList(RequestWaitListDTO requestDTO, Employee employee);
}
