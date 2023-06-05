package com.example.onekids_project.request.createnotifyschool;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class CreateStudentNotify {
    private String sendType;
    private String sendTitle;
    private String sendContent;
    //    private Integer receivedNumber;
    private String attachFile;
    private List<MultipartFile> multipartFileList;
    private int countAttachFile;
    private String attachPicture;
    private int countAttachPicture;

    private boolean sendDel;
    private boolean isApproved;
    private List<Long> dataGradeNotifyList;
    private List<Long> dataClassNotifyList;
    private List<Long> dataGroupNotifyList;
    private List<Long> dataKidNotifyList;
}
