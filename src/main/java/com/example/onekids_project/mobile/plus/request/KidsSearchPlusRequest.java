package com.example.onekids_project.mobile.plus.request;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class KidsSearchPlusRequest {
    @NotNull
    private Long idKid;

    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate date;

//    @NotNull
//    private Long idClass;
}
