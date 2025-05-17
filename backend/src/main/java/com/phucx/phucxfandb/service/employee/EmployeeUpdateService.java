package com.phucx.phucxfandb.service.employee;

import com.phucx.phucxfandb.dto.request.RequestEmployeeDTO;
import com.phucx.phucxfandb.dto.response.EmployeeDTO;

public interface EmployeeUpdateService {
    EmployeeDTO updateEmployeeByUsername(String username, RequestEmployeeDTO requestEmployeeDTO);
}
