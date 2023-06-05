package com.example.onekids_project.request.mauser;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.request.base.IdRequest;
import com.example.onekids_project.validate.anotationcustom.StringInList;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Data
public class ProfileRequest extends IdRequest {
    @NotBlank
    private String fullName;

    @NotBlank
    @StringInList(values = {AppConstant.MALE, AppConstant.FEMALE})
    private String gender;

    @NotBlank
    private String phone;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate birthday;

    private String email;

    private MultipartFile multipartFile;
}
