package com.example.onekids_project.response.chart;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * date 2021-10-05 10:47 AM
 *
 * @author nguyễn văn thụ
 */
@Getter
@Setter
public class ListChartStatusEmployeeResponse {

    List<Long> statusAll;

    List<ChartStatusEmployeeResponse> dataList;
}
