package com.example.onekids_project.request.smsNotify;

import com.example.onekids_project.response.excel.ExcelData;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * date 2021-06-12 9:15 AM
 *
 * @author nguyễn văn thụ
 */
@Getter
@Setter
public class SmsCustomRequest {
    @NotBlank
    private String title;

    @NotEmpty
    private List<ExcelData> bodyList;
}
