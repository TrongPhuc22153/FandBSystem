package com.phucx.phucxfandb.service.employee;

import com.phucx.phucxfandb.dto.response.EmployeeDTO;
import com.phucx.phucxfandb.entity.Employee;

public interface EmployeeReaderService {
    // get employee
    EmployeeDTO getEmployeeDetailsByUsername(String username);
    Employee getEmployeeEntityByUsername(String username);

}
