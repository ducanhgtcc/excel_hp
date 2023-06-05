package com.example.onekids_project.mobile.teacher.request.evaluate;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class EvaluateCommonCreateTeacherRequest {
    @NotNull
    private List<Long> idList;

    @NotNull
    private String content;

    private List<MultipartFile> multipartFileList;

}
