package com.example.onekids_project.request.smsNotify;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class SmsNotifyRequest {

    private String sendTitle;
    @NotBlank
    private String sendContent;

//    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @DateTimeFormat (pattern = "yyyy-MM-dd - HH:mm")
    private LocalDateTime dateTime;

    private boolean timer;

    private List<Long> dataGradeNotifySmsList;
    private List<Long> dataClassNotifySmsList;
    private List<Long> dataGroupNotifySmsList;
    private List<Long> dataKidNotifySmsList;
    private List<Long> dataEmployeeNotifySmsList;
    private List<Long> dataDepartmentNotifySmsList;
}
