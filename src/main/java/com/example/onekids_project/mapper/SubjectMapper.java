package com.example.onekids_project.mapper;

import com.example.onekids_project.dto.EmployeeDTO;
import com.example.onekids_project.dto.SubjectDTO;
import com.example.onekids_project.entity.employee.Employee;
import com.example.onekids_project.entity.employee.Subject;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SubjectMapper {

    @Autowired
    private ModelMapper modelMapper;

    public SubjectDTO mapperToDTO(Subject subject){

        SubjectDTO result= modelMapper.map(subject,SubjectDTO.class);

        return  result;
    }

    public Subject mapperToEntity(SubjectDTO  subjectDTO){

        Subject result= modelMapper.map(subjectDTO,Subject.class);

        return  result;
    }


}
