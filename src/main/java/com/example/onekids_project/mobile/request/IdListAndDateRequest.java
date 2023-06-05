package com.example.onekids_project.mobile.request;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

/**
 * date 2021-02-24 9:44 SA
 *
 * @author ADMIN
 */
@Data
public class IdListAndDateRequest {
    @NotEmpty
    private List<Long> idList;

    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate date;
}
