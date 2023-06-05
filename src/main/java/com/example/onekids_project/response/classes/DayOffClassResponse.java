package com.example.onekids_project.response.classes;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * date 2021-05-05 14:39
 *
 * @author lavanviet
 */
@Getter
@Setter
public class DayOffClassResponse extends IdResponse {
    private LocalDate date;

    private String note;
}
