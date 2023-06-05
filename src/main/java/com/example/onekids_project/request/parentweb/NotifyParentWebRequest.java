package com.example.onekids_project.request.parentweb;

import com.example.onekids_project.request.base.PageNumberWebRequest;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

/**
 * @author lavanviet
 */
@Data
public class NotifyParentWebRequest extends PageNumberWebRequest {

    private String title;

    private String content;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private List<LocalDate> dateList;
}
