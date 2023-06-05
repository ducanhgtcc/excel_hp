package com.example.onekids_project.service.serviceimpl;

import com.example.onekids_project.request.base.IdRequest;
import com.example.onekids_project.request.classes.ExEmployeeClassRequest;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ListExEmployeeClassRequest extends IdRequest {
    List<ExEmployeeClassRequest> exEmployeeClassRequestList;
}
