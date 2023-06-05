package com.example.onekids_project.mapper;

import com.example.onekids_project.dto.EmployeeDTO;
import com.example.onekids_project.dto.ExDepartmentEmployeeDTO;
import com.example.onekids_project.entity.employee.Employee;
import com.example.onekids_project.entity.school.ExDepartmentEmployee;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ExDepartmentEmployeeMapper {

    @Autowired
    private ModelMapper modelMapper;

    public ExDepartmentEmployeeDTO mapperToDTO(ExDepartmentEmployee exDepartmentEmployee){

        ExDepartmentEmployeeDTO result= modelMapper.map(exDepartmentEmployee,ExDepartmentEmployeeDTO.class);

        return  result;
    }

    public ExDepartmentEmployee mapperToEntity(ExDepartmentEmployeeDTO  exDepartmentEmployeeDTO){

        ExDepartmentEmployee result= modelMapper.map(exDepartmentEmployeeDTO,ExDepartmentEmployee.class);

        return  result;
    }
}
