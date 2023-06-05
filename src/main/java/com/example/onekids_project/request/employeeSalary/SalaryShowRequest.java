package com.example.onekids_project.request.employeeSalary;

import com.example.onekids_project.request.base.IdRequest;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * date 2021-02-23 2:42 CH
 *
 * @author ADMIN
 */

@Data
public class SalaryShowRequest extends IdRequest {

    @NotNull
    private LocalDate date;
}
