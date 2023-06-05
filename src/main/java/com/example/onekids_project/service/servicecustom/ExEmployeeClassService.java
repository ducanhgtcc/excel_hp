package com.example.onekids_project.service.servicecustom;

import com.example.onekids_project.response.classes.ExEmployeeClassResponse;
import com.example.onekids_project.service.serviceimpl.ListExEmployeeClassRequest;

import java.util.List;


public interface ExEmployeeClassService {
    List<ExEmployeeClassResponse> findByIdExEmployeeClass(Long idSchool, Long idClass);

    boolean updateExEmployeeClass(Long idSchool, ListExEmployeeClassRequest listExEmployeeClassRequest);
}
