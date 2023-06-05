package com.example.onekids_project.request.AppSend;

import com.example.onekids_project.request.base.PageNumberWebRequest;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class SearchContentRequest extends PageNumberWebRequest {

    private String sendContent;

    private Boolean userUnread;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private List<LocalDate> dateStartEnd;

    @Override
    public String toString() {
        return "SearchContentRequest{" +
                "sendContent='" + sendContent + '\'' +
                ", userUnread=" + userUnread +
                ", dateStartEnd=" + dateStartEnd +
                "} " + super.toString();
    }
}
