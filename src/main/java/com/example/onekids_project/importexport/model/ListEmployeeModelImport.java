package com.example.onekids_project.importexport.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ListEmployeeModelImport {
    List<EmployeeModelImport> employeeModelImportList;
    List<EmployeeModelImportFail> employeeModelImportFailList;
}
