package com.example.onekids_project.request.employeeSalary;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
public class EmployeeSalaryApplyRequest {

    @NotNull
    private Long idInfoEmployee;

    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate localDate;
}
