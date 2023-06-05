package com.example.onekids_project.request.finance.statistical;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
public class FinanceSearchKidsRequest {

    private String startMonth;

    private String endMonth;

    private String year;

    private Long idClass;

    private String status;

}
