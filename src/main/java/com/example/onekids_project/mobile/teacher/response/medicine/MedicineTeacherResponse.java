package com.example.onekids_project.mobile.teacher.response.medicine;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MedicineTeacherResponse extends IdResponse {

    private String fullName;

    private String content;

    private String createdDate;

    private int replyNumber;

    private int pictureNumber;

    private String diseaseName;

    private String dateSick;

    private String avatar;

    private boolean confirmStatus;

    private boolean teacherUnread;

}
