package com.example.onekids_project.request.classes;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

/**
 * date 2021-05-05 14:05
 *
 * @author lavanviet
 */
@Data
public class DayOffClassManyRequest {
    @NotEmpty
    private List<Long> idClassList;

    @NotEmpty
    private List<LocalDate> dateList;

    @NotBlank
    private String note;

}
