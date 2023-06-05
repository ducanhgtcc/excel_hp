package com.example.onekids_project.mobile.teacher.response.absentteacher;

import com.example.onekids_project.mobile.response.LastPageBase;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * date 2021-05-25 4:46 PM
 *
 * @author nguyễn văn thụ
 */
@Getter
@Setter
public class ListAbsentTeacherMobileResponse extends LastPageBase {

    private List<AbsentTeacherMobileResponse> dataList;
}
