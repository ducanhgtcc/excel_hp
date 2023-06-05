package com.example.onekids_project.request.absentteacher;

import com.example.onekids_project.request.base.PageNumberWebRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * date 2021-05-21 2:03 PM
 *
 * @author nguyễn văn thụ
 */
@Getter
@Setter
@ToString
public class SearchAbsentTeacherRequest extends PageNumberWebRequest {

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate createdDate;

    private Boolean confirmStatus;

    private String content;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate date;

}
