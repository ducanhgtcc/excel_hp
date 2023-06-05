package com.example.onekids_project.mobile.plus.request.notify;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class NotifyPlusRequest {

    @NotEmpty
    @NotNull
    private List<Long> idList;

    @NotBlank
    private String sendContent;

    private List<MultipartFile> multipartFileList;

    private List<MultipartFile> multipartImageList;
}
