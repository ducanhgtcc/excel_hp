package com.example.onekids_project.request.system;

import com.example.onekids_project.common.SampleConstant;
import com.example.onekids_project.request.base.IdRequest;
import com.example.onekids_project.validate.anotationcustom.StringInList;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class BirthdaySampleUpdateRequest extends IdRequest {
    @NotBlank
    private String content;

    private MultipartFile multipartFile;
}
