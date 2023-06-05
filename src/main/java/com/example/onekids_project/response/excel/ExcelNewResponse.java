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
public class ExcelNewResponse {
    private String sheetName;

    private List<ExcelDataNew> headerList = new ArrayList<>();

    private List<ExcelDataNew> bodyList;
}
