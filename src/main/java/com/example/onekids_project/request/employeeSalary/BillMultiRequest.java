package com.example.onekids_project.request.employeeSalary;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

/**
 * date 2021-02-24 2:19 CH
 *
 * @author ADMIN
 */
@Data
public class BillMultiRequest {

    @NotNull
    private Boolean status;

    @NotEmpty
    private List<Long> idList;

    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate date;
}
