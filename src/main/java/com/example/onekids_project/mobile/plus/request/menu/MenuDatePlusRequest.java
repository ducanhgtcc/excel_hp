package com.example.onekids_project.mobile.plus.request.menu;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class MenuDatePlusRequest {

    @NotNull
    private Long idClass;

    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate date;
}

