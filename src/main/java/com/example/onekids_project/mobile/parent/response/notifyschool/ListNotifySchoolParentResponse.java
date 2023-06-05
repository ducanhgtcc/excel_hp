package com.example.onekids_project.mobile.parent.response.notifyschool;

import com.example.onekids_project.mobile.response.LastPageBase;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * date 2021-10-22 10:26 AM
 *
 * @author nguyễn văn thụ
 */
@Getter
@Setter
public class ListNotifySchoolParentResponse extends LastPageBase {

    private List<NotifySchoolParentResponse> dataList;
}
