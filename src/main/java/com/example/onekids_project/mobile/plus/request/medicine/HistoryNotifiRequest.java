package com.example.onekids_project.mobile.plus.request.medicine;

import com.example.onekids_project.mobile.request.PageNumberRequest;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
public class HistoryNotifiRequest extends PageNumberRequest {

    private Boolean confirmStatus;

    private Long idClass;

    private String kidName;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dateDetail;

    private String dateSick;


}

