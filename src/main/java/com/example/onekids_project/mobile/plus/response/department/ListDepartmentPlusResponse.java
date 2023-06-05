package com.example.onekids_project.mobile.plus.response.department;

import com.example.onekids_project.mobile.plus.response.MessagePlusResponse;
import com.example.onekids_project.mobile.response.LastPageBase;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ListDepartmentPlusResponse{

    private List<DepartmentPlusResponse> dataList;

}
