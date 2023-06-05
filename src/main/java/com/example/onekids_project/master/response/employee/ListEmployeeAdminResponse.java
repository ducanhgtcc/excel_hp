package com.example.onekids_project.master.response.employee;

import com.example.onekids_project.response.base.TotalResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ListEmployeeAdminResponse extends TotalResponse {
    private List<EmployeeAdminResponse> dataList;
}
