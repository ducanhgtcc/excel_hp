package com.example.onekids_project.request.smsNotify;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * date 2021-04-05 17:44
 *
 * @author lavanviet
 */
@Data
public class SMSRequest {
    private String sendTitle;

    private String sendContent;

    //hẹn giờ gửi hay không: true là hẹn giờ gửi
    private boolean timer;

    //thời gian hẹn gửi
    private LocalDateTime dateTime;

    private List<Long> idList;
}

