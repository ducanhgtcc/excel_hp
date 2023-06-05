package com.example.onekids_project.response.kids;

import com.example.onekids_project.response.base.TotalResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ListStudentGroupOutResponse extends TotalResponse {
    private List<StudentGroupOutResponse> dataList;
}
