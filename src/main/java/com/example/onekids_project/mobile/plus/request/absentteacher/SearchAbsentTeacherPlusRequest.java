package com.example.onekids_project.mobile.plus.request.absentteacher;

import com.example.onekids_project.mobile.request.PageNumberRequest;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * date 2021-05-31 8:41 AM
 *
 * @author nguyễn văn thụ
 */
@Getter
@Setter
public class SearchAbsentTeacherPlusRequest extends PageNumberRequest {

    private Boolean confirmStatus;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate absentDate;

    private String dateDetail;

    private String teacherName;

}
