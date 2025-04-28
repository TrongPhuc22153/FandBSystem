package com.phucx.phucxfandb.service.employee.imp;

import com.phucx.phucxfandb.dto.request.RequestEmployeeDTO;
import com.phucx.phucxfandb.dto.response.EmployeeDTO;
import com.phucx.phucxfandb.entity.Employee;
import com.phucx.phucxfandb.entity.User;
import com.phucx.phucxfandb.exception.NotFoundException;
import com.phucx.phucxfandb.mapper.EmployeeMapper;
import com.phucx.phucxfandb.repository.EmployeeRepository;
import com.phucx.phucxfandb.repository.UserRepository;
import com.phucx.phucxfandb.service.employee.EmployeeUpdateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmployeeUpdateServiceImpl implements EmployeeUpdateService {
    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;
    private final EmployeeMapper mapper;

    @Override
    @Modifying
    @Transactional
    public EmployeeDTO updateEmployee(String employeeId, RequestEmployeeDTO requestEmployeeDTO) {
        log.info("updateEmployee(employeeId={}, requestEmployeeDTO={})", employeeId, requestEmployeeDTO);
        Employee existingEmployee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new NotFoundException("Employee", employeeId));
        mapper.updateEmployee(requestEmployeeDTO, existingEmployee);
        Employee updatedEmployee = employeeRepository.save(existingEmployee);
        return mapper.toEmployeeDTO(updatedEmployee);
    }

    @Override
    @Modifying
    @Transactional
    public EmployeeDTO updateEmployeeByUsername(String username, RequestEmployeeDTO requestEmployeeDTO) {
        log.info("updateEmployeeByUsername(username={}, requestEmployeeDTO={})", username, requestEmployeeDTO);
        Employee existingEmployee = employeeRepository.findByProfileUserUsernameAndIsDeletedFalse(username)
                .orElseThrow(() -> new NotFoundException("Employee", username));
        mapper.updateEmployee(requestEmployeeDTO, existingEmployee);
        Employee updatedEmployee = employeeRepository.save(existingEmployee);
        return mapper.toEmployeeDTO(updatedEmployee);
    }

    @Override
    @Modifying
    @Transactional
    public void deleteEmployee(String employeeId) {
        log.info("deleteEmployee(employeeId={})", employeeId);
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new NotFoundException("Employee", employeeId));
        User user = employee.getProfile().getUser();
        user.setEnabled(Boolean.FALSE);
        userRepository.save(user);
    }
}
