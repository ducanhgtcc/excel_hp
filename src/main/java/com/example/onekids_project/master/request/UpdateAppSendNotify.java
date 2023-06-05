package com.example.onekids_project.master.request;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class UpdateAppSendNotify extends IdResponse {
//    private String sendType;
    private String sendTitle;
    private String sendContent;
//    private String attachFile;
    private MultipartFile[] multipartFileList;
//    private int countAttachFile;
//    private String attachPicture;
//    private int countAttachPicture;

//    private boolean sendDel;
//    private boolean isApproved;

//    private Long idSchool;
//    private Long idClass;
//    private Long idAgent;
//    private String appTypeArr[];
}
