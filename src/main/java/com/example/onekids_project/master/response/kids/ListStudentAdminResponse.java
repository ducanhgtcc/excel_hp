package com.example.onekids_project.master.response.kids;

import com.example.onekids_project.response.base.TotalResponse;
import com.example.onekids_project.response.kids.StudentResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ListStudentAdminResponse extends TotalResponse {
    private List<StudentAdminResponse> dataList;
}
