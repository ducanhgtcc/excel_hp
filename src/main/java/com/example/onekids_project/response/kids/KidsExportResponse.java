package com.example.onekids_project.response.kids;

import com.example.onekids_project.dto.GradeDTO;
import com.example.onekids_project.dto.MaClassDTO;
import com.example.onekids_project.response.studentgroup.KidsGroupResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KidsExportResponse {

    MaClassDTO maClassDTO;

    GradeDTO gradeDTO;

    String kidsGroup;

    KidsGroupResponse kidsGroupResponse;

    KidMainExportRespone kidMainInfo;

    ParentInforResponse parentInfo;

}
