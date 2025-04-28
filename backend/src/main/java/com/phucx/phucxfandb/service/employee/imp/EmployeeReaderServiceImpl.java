package com.phucx.phucxfandb.service.employee.imp;

import com.phucx.phucxfandb.dto.response.EmployeeDTO;
import com.phucx.phucxfandb.entity.Employee;
import com.phucx.phucxfandb.exception.NotFoundException;
import com.phucx.phucxfandb.mapper.EmployeeMapper;
import com.phucx.phucxfandb.repository.EmployeeRepository;
import com.phucx.phucxfandb.service.employee.EmployeeReaderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmployeeReaderServiceImpl implements EmployeeReaderService {
    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public EmployeeDTO getEmployee(String employeeID) {
        log.info("getEmployee(employeeId={})", employeeID);
        Employee employee = employeeRepository.findByEmployeeIdAndIsDeletedFalse(employeeID)
                .orElseThrow(()-> new NotFoundException("Employee", employeeID));
        return mapper.toEmployeeDTO(employee);
    }

    @Override
    @Transactional(readOnly = true)
    public EmployeeDTO getEmployeeByUserID(String userID) {
        log.info("getEmployeeByUserID(userID={})", userID);
        Employee employee = employeeRepository.findByProfileUserUserIdAndIsDeletedFalse(userID)
                .orElseThrow(()-> new NotFoundException("Employee", "user", "id", userID));
        return mapper.toEmployeeDTO(employee);
    }

    @Override
    @Transactional(readOnly = true)
    public EmployeeDTO getEmployeeDetailsByUsername(String username) {
        log.info("getEmployeeDetailsByUsername(username={})", username);
        Employee employee = employeeRepository.findByProfileUserUsernameAndIsDeletedFalse(username)
                .orElseThrow(()-> new NotFoundException("Employee", "username", username));
        return mapper.toEmployeeDTO(employee);
    }

    @Override
    @Transactional(readOnly = true)
    public Employee getEmployeeEntityById(String employeeId) {
        log.info("getEmployeeEntityById(employeeId={})", employeeId);
        return employeeRepository.findById(employeeId)
                .orElseThrow(()-> new NotFoundException("Employee", "id", employeeId));
    }

    @Override
    @Transactional(readOnly = true)
    public Employee getEmployeeEntityByUsername(String username) {
        log.info("getEmployeeEntityById(username={})", username);
        return employeeRepository.findByProfileUserUsernameAndIsDeletedFalse(username)
                .orElseThrow(()-> new NotFoundException("Employee", "username", username));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EmployeeDTO> getEmployees(Integer pageNumber, Integer pageSize) {
        log.info("getEmployees(pageNumber={}, pageSize={})", pageNumber, pageSize);
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return employeeRepository.findByIsDeletedFalse(pageable)
                .map(mapper::toEmployeeDTO);
    }

}
