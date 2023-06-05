package com.example.onekids_project.response.feedback;

import com.example.onekids_project.entity.user.FeedBackFile;
import com.example.onekids_project.response.base.IdResponse;
import com.example.onekids_project.response.classes.MaClassResponse;
import com.example.onekids_project.response.employee.EmployeeResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class FeedBackResponse extends IdResponse {

    private String feedbackTitle;

    private String feedbackContent;

    private String accountType;

    private boolean hiddenStatus;

    //người góp ý
    private String createdByUser;

    // người xác nhận
    private String userConfirm;

    // người trả lời
    private String userReply;

    private Long idSchoolConfirm;

    private String confirmName;

    private boolean teacherReplyDel;

    private String replyName;

    private Long idSchoolReply;

    private boolean schoolConfirmStatus;

    private boolean schoolUnread;

    private boolean schoolReplyDel;

    private String defaultContentSchool;

    private String schoolReply;

    private LocalDateTime schoolTimeReply;

    private LocalDateTime confirmDate;

    private String createdBy;

    private LocalDateTime createdDate;

    private int countReply;

    private String urlPicture;

    private MaClassResponse maClass;

    private EmployeeResponse employee;

    private int numberFile;

    private List<FeedBackFile> feedBackFileList;
    //phụ huynh tạo thì có idKid
    private Long idKid;
}
