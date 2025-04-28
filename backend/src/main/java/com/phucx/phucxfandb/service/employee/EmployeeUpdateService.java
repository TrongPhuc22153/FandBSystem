package com.phucx.phucxfandb.service.employee;

import com.phucx.phucxfandb.dto.request.RequestEmployeeDTO;
import com.phucx.phucxfandb.dto.response.EmployeeDTO;

public interface EmployeeUpdateService {
    EmployeeDTO updateEmployee(String employeeId, RequestEmployeeDTO requestEmployeeDTO);
    EmployeeDTO updateEmployeeByUsername(String username, RequestEmployeeDTO requestEmployeeDTO);

    void deleteEmployee(String employeeId);
}
