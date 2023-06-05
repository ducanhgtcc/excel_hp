package com.example.onekids_project.request.chart;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.validate.anotationcustom.StringInList;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * date 2021-09-20 11:11 AM
 *
 * @author nguyễn văn thụ
 */
@Getter
@Setter
public class StatusEmployeeChartRequest {

    @NotBlank
    @StringInList(values = {AppConstant.CHART_CLASS, AppConstant.CHART_ALL})
    private String type;

}
