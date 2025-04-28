package com.phucx.phucxfandb.mapper;

import com.phucx.phucxfandb.dto.request.RequestEmployeeDTO;
import com.phucx.phucxfandb.dto.response.EmployeeDTO;
import com.phucx.phucxfandb.entity.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {
    EmployeeDTO toEmployeeDTO(Employee employee);


    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "lastModifiedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "employeeId", ignore = true)
    void updateEmployee(RequestEmployeeDTO requestEmployeeDTO, @MappingTarget Employee employee);
}
