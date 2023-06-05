package com.example.onekids_project.mobile.teacher.request.evaluate;

import com.example.onekids_project.request.base.IdRequest;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class EvaluateDateCommonCreateTeacherRequest{
    @NotNull
    private List<Long> idList;

    @NotNull
    private String learnContent;

    @NotNull
    private String eatContent;

    @NotNull
    private String sleepContent;

    @NotNull
    private String healtContent;

    @NotNull
    private String sanitaryContent;

    @NotNull
    private String commonContent;

    private List<MultipartFile> multipartFileList;

}
