package com.phucx.phucxfandb.mapper;

import com.phucx.phucxfandb.dto.request.RequestTableDTO;
import com.phucx.phucxfandb.dto.response.TableDTO;
import com.phucx.phucxfandb.entity.TableEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface TableMapper {

    TableDTO toTableDTO(TableEntity table);

    @Mapping(target = "isDeleted", constant = "false")
    @Mapping(target = "tableId", ignore = true)
    TableEntity toTable(RequestTableDTO requestTableDTO);

    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "lastModifiedAt", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "tableId", ignore = true)
    void updateTable(RequestTableDTO requestTableDTO, @MappingTarget TableEntity table);


}
