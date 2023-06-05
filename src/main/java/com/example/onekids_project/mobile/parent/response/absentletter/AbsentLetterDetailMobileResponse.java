package com.example.onekids_project.mobile.parent.response.absentletter;

import com.example.onekids_project.entity.kids.AbsentDate;
import com.example.onekids_project.entity.parent.AbsentLetterAttachFile;
import com.example.onekids_project.mobile.response.ReplyMobileObject;
import com.example.onekids_project.response.parentdiary.ListAbsentDateResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class AbsentLetterDetailMobileResponse {

    private String fullName;

    private String avatar;

    private String content;

    private String dateAbsent;

    private List<AbsentDateDataResponse> absentDateList;

    private List<String> pictureList;

    private boolean confirmStatus;

    private LocalDateTime createdDate;

    private List<ReplyMobileObject> replyList;

    private boolean schoolModifyStatus;
//    private List<AbsentLetterAttachFile> absentLetterAttachFileList;
}
