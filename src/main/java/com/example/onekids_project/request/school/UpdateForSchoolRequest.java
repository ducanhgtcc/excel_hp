package com.example.onekids_project.request.school;

import com.example.onekids_project.request.base.IdRequest;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class UpdateForSchoolRequest extends IdRequest {
    @NotBlank
    private String schoolName;

    private String schoolDescription;

    private String schoolAddress;

    private String schoolPhone;

    private String schoolEmail;

//    private String contactName;

//    private String contactDescription;

//    private String contactPhone;

//    private String contactEmail;

    private String schoolWebsite;

    private String contactName1;

//    private String contactDescription1;

    private String contactPhone1;

//    private String contactEmail1;

    private String contactName2;

//    private String contactDescription2;

    private String contactPhone2;

//    private String contactEmail2;

    private String contactName3;

//    private String contactDescription3;

    private String contactPhone3;

//    private String contactEmail3;

    private MultipartFile multipartFile;
}
