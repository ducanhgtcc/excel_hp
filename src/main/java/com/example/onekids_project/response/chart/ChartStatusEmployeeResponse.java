package com.example.onekids_project.response.chart;

import lombok.Getter;
import lombok.Setter;

/**
 * date 2021-09-20 11:14 AM
 *
 * @author nguyễn văn thụ
 */
@Getter
@Setter
public class ChartStatusEmployeeResponse {

    private String name;

    private long statusWorking;

    private long statusRetain;

    private long statusLeave;
}
