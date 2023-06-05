package com.example.onekids_project.mobile.teacher.request.absentteacher;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

/**
 * date 2021-05-21 5:15 PM
 *
 * @author nguyễn văn thụ
 */
@Getter
@Setter
@ToString
public class AbsentTeacherMobileRequest {

    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate fromDate;

    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate toDate;

    @NotBlank
    private String content;

    private List<MultipartFile> multipartFileList;

    @Valid
    private List<AbsentDateRequest> dateList;
}
