package com.example.onekids_project.request.evaluatekids;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class EvaluateDateKidsBriefCommonRequest {
    @NotNull
    private List<Long> idKidList;

    private String learnContent;

    private String eatContent;

    private String sleepContent;

    private String sanitaryContent;

    private String healtContent;

    private String commonContent;

    private List<MultipartFile> multipartFileList;


}
