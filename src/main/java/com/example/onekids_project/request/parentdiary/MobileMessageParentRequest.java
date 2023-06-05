package com.example.onekids_project.request.parentdiary;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class MobileMessageParentRequest {
    private String messageContent;

    private String urlPicture;

    private MultipartFile multipartFile;

}
