package com.example.onekids_project.mobile.plus.request.cashinternal;

import com.example.onekids_project.mobile.request.PageNumberRequest;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * date 2021-06-18 14:13
 *
 * @author lavanviet
 */
@Getter
@Setter
public class CashInternalPlusRequest extends PageNumberRequest {

//    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate startDate;

//    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate endDate;

    private Boolean approved;

    private String code;

}
