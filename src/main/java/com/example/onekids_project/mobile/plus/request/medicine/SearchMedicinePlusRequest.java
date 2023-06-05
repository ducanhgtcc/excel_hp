package com.example.onekids_project.mobile.plus.request.medicine;

import com.example.onekids_project.mobile.request.PageNumberRequest;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
public class SearchMedicinePlusRequest extends PageNumberRequest {

    private Boolean confirmStatus;

    private Long idClass;

    private String kidName;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dateDetail;

    private String dateSick;

    @Override
    public String toString() {
        return "SearchMedicinePlusRequest{" +
                "confirmStatus=" + confirmStatus +
                ", idClass=" + idClass +
                ", kidName='" + kidName + '\'' +
                ", dateDetail=" + dateDetail +
                ", dateSick='" + dateSick + '\'' +
                "} " + super.toString();
    }
}

