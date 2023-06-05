package com.example.onekids_project.request.employee;

import com.example.onekids_project.response.excel.ExcelDataNew;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * date 2021-07-07 8:37 AM
 *
 * @author nguyễn văn thụ
 */
@Getter
@Setter
public class CreateEmployeeExcelRequest {

    @NotEmpty
    private List<ExcelDataNew> bodyList;
}
