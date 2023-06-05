package com.example.onekids_project.request.celebrate;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.common.SampleConstant;
import com.example.onekids_project.request.base.IdRequest;
import com.example.onekids_project.validate.anotationcustom.StringInList;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class CelebrateSampleUpdateRequest extends IdRequest{
    @NotBlank
    private String title;

    @NotBlank
    private String date;

    @NotBlank
    private String month;

    @NotBlank
    @StringInList(values = {SampleConstant.KIDS, SampleConstant.PARENT, SampleConstant.TEACHER, SampleConstant.PLUS,})
    private String type;

    @NotBlank
    private String content;

    @StringInList(values = {AppConstant.MALE, AppConstant.FEMALE, AppConstant.ALL})
    private String gender;

    private MultipartFile multipartFile;
}
