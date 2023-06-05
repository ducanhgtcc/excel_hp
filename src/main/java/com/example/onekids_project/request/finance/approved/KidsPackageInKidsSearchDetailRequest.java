package com.example.onekids_project.request.finance.approved;

import com.example.onekids_project.common.KidsStatusConstant;
import com.example.onekids_project.validate.anotationcustom.StringInList;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class KidsPackageInKidsSearchDetailRequest {

    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate date;

    @NotNull
    private Long idKid;

}
