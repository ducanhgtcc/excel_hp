package com.example.onekids_project.master.request.notify;

import com.example.onekids_project.request.base.PageNumberWebRequest;
import lombok.Getter;
import lombok.Setter;

/**
 * date 2021-08-03 11:13 AM
 *
 * @author nguyễn văn thụ
 */
@Getter
@Setter
public class SearchNotificationRequest extends PageNumberWebRequest {

    private String type;
}
