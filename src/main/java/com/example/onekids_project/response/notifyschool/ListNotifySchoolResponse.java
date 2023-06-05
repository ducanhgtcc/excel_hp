package com.example.onekids_project.response.notifyschool;

import com.example.onekids_project.response.base.TotalResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * date 2021-10-21 9:56 AM
 *
 * @author nguyễn văn thụ
 */
@Getter
@Setter
public class ListNotifySchoolResponse extends TotalResponse {

    List<NotifySchoolResponse> dataList;
}
