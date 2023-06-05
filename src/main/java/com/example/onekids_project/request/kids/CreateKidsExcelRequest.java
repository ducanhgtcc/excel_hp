package com.example.onekids_project.request.kids;

import com.example.onekids_project.response.excel.ExcelDataNew;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * date 2021-07-07 2:59 PM
 *
 * @author nguyễn văn thụ
 */
@Getter
@Setter
public class CreateKidsExcelRequest {
    @NotEmpty
    private List<ExcelDataNew> bodyList;
}
