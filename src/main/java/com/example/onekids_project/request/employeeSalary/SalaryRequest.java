package com.example.onekids_project.request.employeeSalary;

import com.example.onekids_project.request.base.IdRequest;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * date 2021-02-24 9:44 SA
 *
 * @author ADMIN
 */
@Data
public class SalaryRequest extends IdRequest {
    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate date;
}
