package com.example.onekids_project.mobile.teacher.response.medicine;

import com.example.onekids_project.mobile.response.ReplyMobileDateObject;
import com.example.onekids_project.mobile.teacher.response.album.ListPictureOtherRespone;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MedicineTeacherDetailResponse {

    private String fullName;

    private String className;

    private String avatarkid;

    private String avartarParent;

    private String parentName;

    private String  diseaseName;

    private String dateSick;

    private String content;

    private boolean confirmStatus;

    private boolean checkTeacherReply;

    private List<String> pictureList;

    private String createdDate;

    private List<ReplyMobileDateObject> replyList;
}
