package com.example.onekids_project.request.schoolconfig;

import com.example.onekids_project.common.SampleConstant;
import com.example.onekids_project.validate.anotationcustom.StringInList;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class BirthdaySampleCreateRequest {
    @NotBlank
    private String content;

    private boolean smsSend;

    private boolean appSend;

    private boolean active;

    @NotBlank
    @StringInList(values = {SampleConstant.TEACHER, SampleConstant.PARENT, SampleConstant.KIDS})
    private String birthdayType;

    private MultipartFile multipartFile;

}
