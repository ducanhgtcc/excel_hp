package com.example.onekids_project.mobile.teacher.response.notifyschool;

import com.example.onekids_project.mobile.response.LastPageBase;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * date 2021-10-22 10:55 AM
 *
 * @author nguyễn văn thụ
 */
@Getter
@Setter
public class ListNotifySchoolTeacherResponse extends LastPageBase {

    private List<NotifySchoolTeacherResponse> dataList;
}
