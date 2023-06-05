package com.example.onekids_project.mobile.teacher.request.attendacekids;

import com.example.onekids_project.mobile.request.AttendanceStatusDayRequest;
import com.example.onekids_project.request.base.IdRequest;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class AttendanceOneKidArriveTeacherRequest extends IdRequest {

    private String content;

    private MultipartFile picture;

    private boolean deletePicture;

    private String time;

    private AttendanceStatusDayRequest morningList;
    private AttendanceStatusDayRequest afternoonList;
    private AttendanceStatusDayRequest eveningList;

    @Override
    public String toString() {
        return "AttendanceOneKidArriveTeacherRequest{" +
                "content='" + content + '\'' +
                ", picture=" + picture +
                ", deletePicture=" + deletePicture +
                ", time='" + time + '\'' +
                ", morningList=" + morningList +
                ", afternoonList=" + afternoonList +
                ", eveningList=" + eveningList +
                '}';
    }
}
