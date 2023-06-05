package com.example.onekids_project.mobile.teacher.response.phonebook;

import com.example.onekids_project.mobile.request.PageNumberRequest;
import com.example.onekids_project.mobile.response.LastPageBase;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ListContactTeacherResponse extends LastPageBase {
    private List<ContactTeacherResponse> dataList;
}
