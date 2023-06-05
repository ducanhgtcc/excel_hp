package com.example.onekids_project.request.notifihistory;

import com.example.onekids_project.request.base.PageNumberWebRequest;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class SearchHistorySmsSendNewtRequest extends PageNumberWebRequest {

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private List<LocalDate> dateStartEnd;

    @Override
    public String toString() {
        return "SearchHistorySmsSendNewtRequest{" +
                "dateStartEnd=" + dateStartEnd +
                "} " + super.toString();
    }
}
