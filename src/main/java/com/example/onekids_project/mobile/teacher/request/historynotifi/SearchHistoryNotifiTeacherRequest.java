package com.example.onekids_project.mobile.teacher.request.historynotifi;

import com.example.onekids_project.mobile.request.PageNumberRequest;
import lombok.Data;

@Data
public class SearchHistoryNotifiTeacherRequest extends PageNumberRequest {

    private String keyWord;

}
