package com.example.onekids_project.request.feedback;

import com.example.onekids_project.request.base.PageNumberWebRequest;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class SearchParentFeedbackRequest extends PageNumberWebRequest {

    private String feedbackTitle;

    private Boolean hiddenStatus;

    private Boolean schoolUnread;

    private String accountType;

    private String confirmName;

    private String replyName;
//    private LocalDate createdDate;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private List<LocalDate> dateStartEnd=new ArrayList<>();
}
