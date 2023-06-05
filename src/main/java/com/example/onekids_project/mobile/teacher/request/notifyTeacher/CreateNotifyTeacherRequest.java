package com.example.onekids_project.mobile.teacher.request.notifyTeacher;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class CreateNotifyTeacherRequest {
    @NotEmpty
    private List<Long> idList;
    private String sendTitle;
    @NotBlank
    private String sendContent;
    private List<MultipartFile> multipartPictureList;
    private List<MultipartFile> multipartFileList;
}
