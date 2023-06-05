package com.example.onekids_project.mapper;

import com.example.onekids_project.dto.EmployeeDTO;
import com.example.onekids_project.entity.employee.Employee;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EmployeeMapper {

    @Autowired
    private ModelMapper modelMapper;

    public EmployeeDTO mapperToDTO(Employee employee) {

        EmployeeDTO result = modelMapper.map(employee, EmployeeDTO.class);

        return result;
    }

    public Employee mapperToEntity(EmployeeDTO employeeDTO) {

        Employee result = modelMapper.map(employeeDTO, Employee.class);

        return result;
    }
}
