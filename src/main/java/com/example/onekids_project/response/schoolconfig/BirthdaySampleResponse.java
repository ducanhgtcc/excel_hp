package com.example.onekids_project.response.schoolconfig;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
public class BirthdaySampleResponse extends IdResponse {
    private String content;

    private String urlPicture;

    private String birthdayType;

    private boolean smsSend;

    private boolean appSend;

    private boolean active;

}
