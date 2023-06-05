package com.example.onekids_project.mobile.parent.request.kids;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class KidsExtraInforRequest {
    private MultipartFile avatar;

    private String nickName;

    private String bloodType;

    private String swim;

    private String allery;

    private String diet;

    private String ear;

    private String nose;

    private String throat;

    private String eyes;

    private String skin;

    private String heart;

    private String fat;
}
