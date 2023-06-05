package com.example.onekids_project.mobile.teacher.response.notify;

import com.example.onekids_project.mobile.response.LastPageBase;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class ListMobileNotifyTeacherResponse extends LastPageBase {

    private List<MobileNotifyTecaherResponse> dataList;
}
