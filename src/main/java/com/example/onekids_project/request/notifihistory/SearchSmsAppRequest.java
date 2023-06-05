package com.example.onekids_project.request.notifihistory;

import com.example.onekids_project.request.base.PageNumberWebRequest;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class SearchSmsAppRequest extends PageNumberWebRequest {

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private List<LocalDate> dateStartEnd;

    private String dateSick;

    private String receiverType;

    private String title;

    @Override
    public String toString() {
        return "SearchSmsAppRequest{" +
                "dateStartEnd=" + dateStartEnd +
                ", dateSick='" + dateSick + '\'' +
                ", receiverType='" + receiverType + '\'' +
                ", title='" + title + '\'' +
                "} " + super.toString();
    }
}
