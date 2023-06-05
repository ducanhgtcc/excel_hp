package com.example.onekids_project.response.absentteacher;

import com.example.onekids_project.entity.employee.AbsentTeacherAttackFile;
import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * date 2021-05-21 2:10 PM
 *
 * @author nguyễn văn thụ
 */
@Getter
@Setter
public class AbsentTeacherResponse extends IdResponse {
    //Ngày tạo đơn
    private LocalDateTime createdDate;

    //ngày bắt đầu
    private LocalDate fromDate;

    //ngày kết thúc
    private LocalDate toDate;

    //nội dung xin nghỉ
    private String content;

    //trạng thái xác nhận
    //true là đã xác nhận
    private boolean confirmStatus;

    //nhà trường đọc, true là nhà trường đã đọc
    private boolean schoolRead;

    //id người phản hồi
    private Long idSchoolReply;

    //Đếm số file
    private int numberFile;

    //Người tạo
    private String createdBy;


}
