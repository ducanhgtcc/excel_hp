package com.example.onekids_project.mobile.plus.response.absentteacher;

import com.example.onekids_project.mobile.response.LastPageBase;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * date 2021-05-31 9:13 AM
 *
 * @author nguyễn văn thụ
 */
@Getter
@Setter
public class ListAbsentTeacherPlusResponse extends LastPageBase {

    private List<AbsentTeacherPlusResponse> dataList;
}
