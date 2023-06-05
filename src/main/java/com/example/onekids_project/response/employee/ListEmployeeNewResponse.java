package com.example.onekids_project.response.employee;

import com.example.onekids_project.response.base.TotalResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ListEmployeeNewResponse extends TotalResponse {
    private List<EmployeeNewResponse> dataList;
}
