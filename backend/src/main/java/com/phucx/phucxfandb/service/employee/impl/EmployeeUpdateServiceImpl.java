package com.phucx.phucxfandb.service.employee.impl;

import com.phucx.phucxfandb.dto.request.RequestEmployeeDTO;
import com.phucx.phucxfandb.dto.response.EmployeeDTO;
import com.phucx.phucxfandb.entity.Employee;
import com.phucx.phucxfandb.exception.NotFoundException;
import com.phucx.phucxfandb.mapper.EmployeeMapper;
import com.phucx.phucxfandb.repository.EmployeeRepository;
import com.phucx.phucxfandb.service.employee.EmployeeUpdateService;
import com.phucx.phucxfandb.service.user.UserProfileUpdateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmployeeUpdateServiceImpl implements EmployeeUpdateService {
    private final UserProfileUpdateService userProfileUpdateService;
    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper mapper;

    @Override
    @Transactional
    public EmployeeDTO updateEmployeeByUsername(String username, RequestEmployeeDTO requestEmployeeDTO) {
        Employee existingEmployee = employeeRepository.findByProfileUserUsernameAndIsDeletedFalse(username)
                .orElseThrow(() -> new NotFoundException(Employee.class.getSimpleName(), username));

        userProfileUpdateService.updateUserProfile(username, requestEmployeeDTO.getProfile());

        mapper.updateEmployee(requestEmployeeDTO, existingEmployee);
        Employee updatedEmployee = employeeRepository.save(existingEmployee);
        return mapper.toEmployeeDTO(updatedEmployee);
    }
}
