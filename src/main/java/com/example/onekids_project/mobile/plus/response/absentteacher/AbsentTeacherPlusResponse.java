package com.example.onekids_project.mobile.plus.response.absentteacher;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * date 2021-05-31 8:51 AM
 *
 * @author nguyễn văn thụ
 */
@Getter
@Setter
public class AbsentTeacherPlusResponse extends IdResponse {

    private String fullName;

    private String content;

    private String createdDate;

    private int replyNumber;

    private int pictureNumber;

    private String absentDate;

    private String avatar;

    private boolean confirmStatus;

    private boolean teacherRead;
}
