package com.example.onekids_project.request.attendancekids;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class AttendanceArriveUpdateContentRequest {
    private String arriveContent;

    private MultipartFile multipartFile;

    @Override
    public String toString() {
        return "AttendanceArriveUpdateContentRequest{" +
                "arriveContent='" + arriveContent + '\'' +
                ", multipartFile=" + multipartFile +
                '}';
    }
}
