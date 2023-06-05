package com.example.onekids_project.request.notifyschool;

import com.example.onekids_project.request.base.PageNumberWebRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

/**
 * date 2021-10-21 9:47 AM
 *
 * @author nguyễn văn thụ
 */
@Getter
@Setter
@ToString
public class SearchNotifySchoolRequest extends PageNumberWebRequest {

    private Boolean active;

    private String title;

    private String content;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private List<LocalDate> dateList;
}
