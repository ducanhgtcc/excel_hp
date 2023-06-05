package com.example.onekids_project.mobile.teacher.response.absentteacher;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

/**
 * date 2021-05-25 4:46 PM
 *
 * @author nguyễn văn thụ
 */
@Getter
@Setter
public class AbsentTeacherMobileResponse extends IdResponse{

    private String content;

    private String createdDate;

    //Số lần phản hồi
    private int replyNumber;

    //Số ảnh
    private int pictureNumber;

    //Ngày nghỉ
    private String dateAbsent;

    //trạng thái xác nhận
    //true là đã xác nhận
    private boolean confirmStatus;

    private boolean teacherRead;
}
