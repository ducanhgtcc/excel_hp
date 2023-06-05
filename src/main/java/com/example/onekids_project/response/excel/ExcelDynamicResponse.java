package com.example.onekids_project.response.excel;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * date 2021-05-12 16:58
 *
 * @author nguyen van thu
 */
@Getter
@Setter
public class ExcelDynamicResponse {
    private String fileName;

    private List<String> titleHeaderList;

    private List<String> proList;

    private List<Integer> sizeColumnList;

    private List<ExcelNewResponse> dataList;
}
