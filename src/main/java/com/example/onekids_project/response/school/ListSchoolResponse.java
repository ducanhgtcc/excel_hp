package com.example.onekids_project.response.school;

import com.example.onekids_project.dto.SchoolDTO;
import com.example.onekids_project.response.base.TotalResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ListSchoolResponse extends TotalResponse {
    List<SchoolDTO> schoolList;
}
