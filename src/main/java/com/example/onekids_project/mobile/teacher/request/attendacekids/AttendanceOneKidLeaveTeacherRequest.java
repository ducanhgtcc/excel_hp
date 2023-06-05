package com.example.onekids_project.mobile.teacher.request.attendacekids;

import com.example.onekids_project.request.base.IdRequest;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class AttendanceOneKidLeaveTeacherRequest extends IdRequest {

    private String content;

    private MultipartFile picture;

    private boolean deletePicture;

    private String time;

    @Override
    public String toString() {
        return "AttendanceOneKidLeaveTeacherRequest{" +
                "content='" + content + '\'' +
                ", picture=" + picture +
                ", deletePicture=" + deletePicture +
                ", time='" + time + '\'' +
                '}';
    }
}
