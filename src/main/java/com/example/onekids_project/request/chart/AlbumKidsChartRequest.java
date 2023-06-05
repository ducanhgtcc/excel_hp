package com.example.onekids_project.request.chart;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.validate.anotationcustom.StringInList;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

/**
 * date 2021-09-15 12:24 PM
 *
 * @author nguyễn văn thụ
 */
@Getter
@Setter
public class AlbumKidsChartRequest {

    private Long idClass;

    private Long idGrade;

    @StringInList(values = {AppConstant.CHART_GRADE, AppConstant.CHART_CLASS})
    private String typeSchool;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate date;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private List<LocalDate> dateList;
}
