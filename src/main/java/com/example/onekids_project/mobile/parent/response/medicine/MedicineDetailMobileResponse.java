package com.example.onekids_project.mobile.parent.response.medicine;

import com.example.onekids_project.mobile.response.ReplyMobileObject;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class MedicineDetailMobileResponse {

    private String fullName;

    private String avatar;

    private String content;

    private String disease_name;

    private String dateMedicine;

    private List<String> pictureList;

    private boolean schoolModifyStatus;

    private boolean confirmStatus;

    private LocalDateTime createdDate;

    private List<ReplyMobileObject> replyList;
}
