package com.example.onekids_project.mobile.plus.request.evaluate;

import com.example.onekids_project.request.base.IdRequest;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class EvaluateDateCreatePlusRequest extends IdRequest {

    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime dateTime;

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

    private List<Long> idFileDeleteList;
}
