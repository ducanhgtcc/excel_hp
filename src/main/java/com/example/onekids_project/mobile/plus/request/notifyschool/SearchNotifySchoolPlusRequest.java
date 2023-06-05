package com.example.onekids_project.mobile.plus.request.notifyschool;

import com.example.onekids_project.mobile.request.PageNumberRequest;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * date 2021-10-22 11:05 AM
 *
 * @author nguyễn văn thụ
 */
@Getter
@Setter
public class SearchNotifySchoolPlusRequest extends PageNumberRequest {

    private Boolean active;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate startDate;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate endDate;
}
