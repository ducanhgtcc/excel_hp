package com.example.onekids_project.mobile.teacher.request.absent;

import com.example.onekids_project.mobile.request.PageNumberRequest;
import lombok.Data;

@Data
public class SearchAbsentTeacherRequest extends PageNumberRequest {

    private Boolean confirmStatus;

    private String keyWord;

    private String dateDetail;

    private String dateAbsent;
}
