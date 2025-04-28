package com.phucx.phucxfandb.service.employee;

import com.phucx.phucxfandb.dto.response.EmployeeDTO;
import com.phucx.phucxfandb.entity.Employee;
import org.springframework.data.domain.Page;

public interface EmployeeReaderService {
    // get employee
    EmployeeDTO getEmployee(String employeeID);
    EmployeeDTO getEmployeeByUserID(String userID);
    EmployeeDTO getEmployeeDetailsByUsername(String username);
    Employee getEmployeeEntityById(String employeeId);
    Employee getEmployeeEntityByUsername(String username);

    Page<EmployeeDTO> getEmployees(Integer pageNumber, Integer pageSize);
}
