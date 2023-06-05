package com.example.onekids_project.mobile.plus.response.notifyschool;

import com.example.onekids_project.mobile.response.LastPageBase;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * date 2021-10-22 11:11 AM
 *
 * @author nguyễn văn thụ
 */
@Getter
@Setter
public class ListNotifySchoolPlusResponse extends LastPageBase {

    List<NotifySchoolPlusResponse> dataList;
}
