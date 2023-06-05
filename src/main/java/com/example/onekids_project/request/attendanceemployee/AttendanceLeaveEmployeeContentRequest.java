package com.example.onekids_project.request.attendanceemployee;

import com.example.onekids_project.request.base.IdRequest;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * date 2021-03-15 11:38
 *
 * @author Phạm Ngọc Thắng
 */

@Data
public class AttendanceLeaveEmployeeContentRequest extends IdRequest {

    private MultipartFile multipartFile;

    private String leaveContent;
}
