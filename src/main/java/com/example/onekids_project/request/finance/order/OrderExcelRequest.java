package com.example.onekids_project.request.finance.order;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

/**
 * @author lavanviet
 */
@Data
public class OrderExcelRequest {
    @NotNull
    private Long idClass;

    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate date;

    @NotEmpty
    private List<Long> idKidList;
}
