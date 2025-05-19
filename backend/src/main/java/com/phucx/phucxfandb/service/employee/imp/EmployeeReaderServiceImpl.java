package com.phucx.phucxfandb.service.employee.imp;

import com.phucx.phucxfandb.dto.response.EmployeeDTO;
import com.phucx.phucxfandb.entity.Employee;
import com.phucx.phucxfandb.exception.NotFoundException;
import com.phucx.phucxfandb.mapper.EmployeeMapper;
import com.phucx.phucxfandb.repository.EmployeeRepository;
import com.phucx.phucxfandb.service.employee.EmployeeReaderService;
import com.phucx.phucxfandb.service.image.ImageReaderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmployeeReaderServiceImpl implements EmployeeReaderService {
    private final EmployeeRepository employeeRepository;
    private final ImageReaderService imageReaderService;
    private final EmployeeMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public EmployeeDTO getEmployeeDetailsByUsername(String username) {
        return employeeRepository.findByProfileUserUsernameAndIsDeletedFalse(username)
                .map(this::setImageUrl)
                .map(mapper::toEmployeeDTO)
                .orElseThrow(()-> new NotFoundException(Employee.class.getSimpleName(), "username", username));
    }

    @Override
    @Transactional(readOnly = true)
    public Employee getEmployeeEntityByUsername(String username) {
        return employeeRepository.findByProfileUserUsernameAndIsDeletedFalse(username)
                .orElseThrow(()-> new NotFoundException(Employee.class.getSimpleName(), "username", username));
    }

    private Employee setImageUrl(Employee employee){
        if(!(employee.getProfile().getPicture()==null || employee.getProfile().getPicture().isEmpty())){
            String imageUrl = imageReaderService.getImageUrl(employee.getProfile().getPicture());
            employee.getProfile().setPicture(imageUrl);
        }
        return employee;
    }

}
