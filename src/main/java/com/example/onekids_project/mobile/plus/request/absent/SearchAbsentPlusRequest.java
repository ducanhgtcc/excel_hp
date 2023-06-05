package com.example.onekids_project.mobile.plus.request.absent;

import com.example.onekids_project.mobile.request.PageNumberRequest;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
public class SearchAbsentPlusRequest extends PageNumberRequest {

    private Boolean confirmStatus;

    private Long idClass;

    private String kidName;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dateDetail;

    private String dateAbsent;

    @Override
    public String toString() {
        return "SearchAbsentPlusRequest{" +
                "confirmStatus=" + confirmStatus +
                ", idClass=" + idClass +
                ", kidName='" + kidName + '\'' +
                ", dateDetail=" + dateDetail +
                ", dateAbsent=" + dateAbsent +
                "} " + super.toString();
    }
}

