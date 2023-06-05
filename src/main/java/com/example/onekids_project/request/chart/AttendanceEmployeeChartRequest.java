package com.example.onekids_project.request.chart;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.validate.anotationcustom.StringInList;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.List;

/**
 * date 2021-09-08 11:15 AM
 *
 * @author nguyễn văn thụ
 */
@Getter
@Setter
public class AttendanceEmployeeChartRequest {

    private Long idDepartment;

    @NotBlank
    @StringInList(values = {AppConstant.CHART_DATE, AppConstant.CHART_WEEK, AppConstant.CHART_MONTH, AppConstant.CHART_YEAR})
    private String type;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private List<LocalDate> dateList;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate week;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate month;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate year;
}
