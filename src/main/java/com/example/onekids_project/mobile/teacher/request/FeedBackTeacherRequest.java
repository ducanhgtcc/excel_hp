package com.example.onekids_project.mobile.teacher.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class FeedBackTeacherRequest {
    @NotBlank
    private String title;

    @NotBlank
    private String content;

    @NotNull
    private Boolean hiddenStatus;

    //tối đa 3 ảnh
    private List<MultipartFile> pictureList;
}

