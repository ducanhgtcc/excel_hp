package com.example.onekids_project.response.chart;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * date 2021-10-05 9:12 AM
 *
 * @author nguyễn văn thụ
 */
@Getter
@Setter
public class ListChartStatusKidsResponse {

    private List<Long> statusAll;

    private List<ChartStatusKidsResponse> dataList;
}
