package com.example.onekids_project.response.excel;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * date 2021-05-12 16:58
 *
 * @author lavanviet
 */
@Getter
@Setter
public class ExcelResponse {
    private String sheetName;

    private List<ExcelData> headerList = new ArrayList<>();

    private List<ExcelData> bodyList;
}
