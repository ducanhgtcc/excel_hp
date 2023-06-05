package com.example.onekids_project.request.school;

import com.example.onekids_project.common.AppConstant;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class CreateSchoolRequest {

    @NotNull
    private Long idAgent;

    @NotBlank
    private String schoolName;

    private String schoolDescription;

    @NotBlank
    private String schoolAddress;

    @NotBlank
    private String schoolPhone;

    private String schoolEmail;

    private String schoolWebsite;

    private String contactName1;

    private String contactDescription1;

    private String contactPhone1;

    private String contactEmail1;

    private String contactName2;

    private String contactDescription2;

    private String contactPhone2;

    private String contactEmail2;

    private String contactName3;

    private String contactDescription3;

    private String contactPhone3;

    private String contactEmail3;

    private Boolean schoolActive;

    private Long smsBudget;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDateTime smsBudgetDate;

    private Boolean smsActiveMore;

    private Long smsUsed;

    private Long smsTotal;

    private String idsmsBrand;

    private String namePhone1;

    private String namePhone2;

    private String namePhone3;

    private boolean trialStatus;


    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate demoStart;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate demoEnd;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dateContractStart;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dateContractEnd;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dateStart;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dateEnd;

    private Boolean limitTime;

    private Boolean limitDevice;

    private Integer numberDevice;

    private MultipartFile multipartFileAvatar;
}
