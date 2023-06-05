package com.example.onekids_project.request.attendanceemployee;

import com.example.onekids_project.request.base.IdRequest;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * date 2021-03-12 14:04
 *
 * @author Phạm Ngọc Thắng
 */

@Data
public class AttendanceEmployeeContentRequest extends IdRequest {

    private MultipartFile multipartFile;

    private String arriveContent;
}
