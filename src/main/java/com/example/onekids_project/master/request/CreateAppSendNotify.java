package com.example.onekids_project.master.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
public class CreateAppSendNotify {

    private String sendType;
    @NotBlank
    private String sendTitle;
    @NotBlank
    private String sendContent;
    private String attachFile;
    private MultipartFile[] multipartFileList;
    private int countAttachFile;
    private String attachPicture;
    private int countAttachPicture;

    private boolean sendDel;
    private boolean isApproved;

    @NotNull
    private Long idSchool;
    @NotEmpty
    private List<String> appTypeArr;
}
