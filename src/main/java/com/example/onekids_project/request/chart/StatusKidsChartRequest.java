package com.example.onekids_project.request.chart;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.validate.anotationcustom.StringInList;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * date 2021-09-20 11:11 AM
 *
 * @author nguyễn văn thụ
 */
@Getter
@Setter
public class StatusKidsChartRequest {

    //grade - class
    @StringInList(values = {AppConstant.CHART_GRADE, AppConstant.CHART_CLASS})
    private String typeSchool;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate date;

}
