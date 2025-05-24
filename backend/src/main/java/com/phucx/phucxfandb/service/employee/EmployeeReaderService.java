package com.phucx.phucxfandb.service.employee;

import com.phucx.phucxfandb.dto.response.EmployeeDTO;
import com.phucx.phucxfandb.entity.Employee;

public interface EmployeeReaderService {
    EmployeeDTO getEmployeeDetailsByUsername(String username);

    Employee getEmployeeEntityById(String employeeId);

    Employee getEmployeeEntityByUsername(String username);

}
