package com.example.onekids_project.request.finance;

import com.example.onekids_project.common.KidsStatusConstant;
import com.example.onekids_project.validate.anotationcustom.StringInList;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Data
public class KidsPackageInKidsSearchRequest {

    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate date;

    @NotBlank
    @StringInList(values = {KidsStatusConstant.STUDYING, KidsStatusConstant.STUDY_WAIT, KidsStatusConstant.RESERVE, KidsStatusConstant.LEAVE_SCHOOL})
    private String status;

    @NotNull
    private Long idClass;

    private String fullName;

    private boolean walletStatus;

    private List<Long> idKidList;

}
