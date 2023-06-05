package com.example.onekids_project.mapper;

import com.example.onekids_project.dto.DepartmentDTO;
import com.example.onekids_project.entity.school.Department;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DepartmentMapper {

    @Autowired
    private ModelMapper modelMapper;

    public DepartmentDTO mapperToDTO(Department department) {

        DepartmentDTO result = modelMapper.map(department, DepartmentDTO.class);

        return result;
    }

    public Department mapperToEntity(DepartmentDTO departmentDTO) {

        Department result = modelMapper.map(departmentDTO, Department.class);

        return result;
    }
}
