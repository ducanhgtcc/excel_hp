package com.example.onekids_project.request.evaluatekids;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
public class EvaluatePeriodicCreateRequest {
    private String content;

    private List<MultipartFile> multipartFileList;

}
