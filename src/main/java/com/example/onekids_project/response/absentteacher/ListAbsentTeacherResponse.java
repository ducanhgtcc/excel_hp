package com.example.onekids_project.response.absentteacher;

import com.example.onekids_project.response.base.TotalResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * date 2021-05-24 8:39 AM
 *
 * @author nguyễn văn thụ
 */
@Getter
@Setter
public class ListAbsentTeacherResponse extends TotalResponse {
    List<AbsentTeacherResponse> absentList;
}
