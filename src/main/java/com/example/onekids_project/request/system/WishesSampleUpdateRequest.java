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
public class WishesSampleUpdateRequest extends IdRequest {
    @NotBlank
    private String wishesContent;

    @NotBlank
    @StringInList(values = {SampleConstant.KIDS, SampleConstant.PARENT, SampleConstant.TEACHER, SampleConstant.PLUS,})
    private String receiverType;

    private MultipartFile multipartFile;
}
