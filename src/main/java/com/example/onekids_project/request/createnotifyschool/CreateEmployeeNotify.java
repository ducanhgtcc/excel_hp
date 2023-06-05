package com.example.onekids_project.request.createnotifyschool;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
public class CreateEmployeeNotify {

    private String sendType;
    private String sendTitle;
    private String sendContent;
    private String attachFile;
    private MultipartFile[] multipartFileList;
    private int countAttachFile;
    private String attachPicture;
    private int countAttachPicture;

    private boolean sendDel;
    private boolean isApproved;
    private List<Long> dataDepartmentNotifyList;
    private List<Long> dataEmployeeNotifyList;
}
