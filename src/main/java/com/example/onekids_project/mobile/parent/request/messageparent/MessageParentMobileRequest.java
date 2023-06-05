package com.example.onekids_project.mobile.parent.request.messageparent;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
public class MessageParentMobileRequest {
    @NotBlank
    private String content;

    private List<MultipartFile> multipartFileList;
}
