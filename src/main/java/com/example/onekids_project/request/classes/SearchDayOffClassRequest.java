package com.example.onekids_project.request.classes;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * date 2021-05-05 16:36
 *
 * @author lavanviet
 */
@Data
public class SearchDayOffClassRequest {
    private Integer year;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate date;

    private String note;
}
