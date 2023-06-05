package com.example.onekids_project.mobile.teacher.request.evaluate;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class EvaluatePeriodicCommonCreateTeacherRequest {
    @NotNull
    @Valid
    private List<IdObjectRequest> idObjectList;

    @NotNull
    private String content;

    private List<MultipartFile> multipartFileList;
}
