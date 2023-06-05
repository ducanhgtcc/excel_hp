package com.example.onekids_project.master.response.school;

import com.example.onekids_project.response.base.TotalResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ListSchoolMasterResponse extends TotalResponse {
    private List<SchoolMasterResponse> dataList;
}
