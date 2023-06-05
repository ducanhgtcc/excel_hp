package com.example.onekids_project.request.employeeSalary;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

/**
 * date 2021-04-05 14:28
 *
 * @author lavanviet
 */
@Getter
@Setter
@ToString
public class GenerateEmployeeSalaryFromSchoolRequest {
    @NotNull
    private LocalDate date;

    @NotEmpty
    private List<Long> idEmployeeList;

    @NotEmpty
    private List<Long> idSalaryList;
}
