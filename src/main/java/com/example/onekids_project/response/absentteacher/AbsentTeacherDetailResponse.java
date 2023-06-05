package com.example.onekids_project.response.absentteacher;

import com.example.onekids_project.response.base.IdResponse;
import com.example.onekids_project.response.common.FileResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * date 2021-05-24 9:39 AM
 *
 * @author nguyễn văn thụ
 */
@Getter
@Setter
public class AbsentTeacherDetailResponse extends IdResponse {

    private LocalDateTime createdDate;

    private String createdBy;

    private String content;

    private LocalDate fromDate;

    private LocalDate toDate;

    private List<FileResponse> fileList;

    private boolean confirmStatus;

    private String schoolReply;

    //thời gian nhà trường phản hồi, lấy từ hệ thống
    private LocalDateTime schoolTimeReply;

    //true: nhà trường đã thu hồi phản hồi
    private boolean schoolReplyDel;

    //true là nhà trường đã sửa phản hồi, false là chưa sửa
//    private boolean schoolModifyStatus;

}
