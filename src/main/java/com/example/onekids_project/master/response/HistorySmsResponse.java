package com.example.onekids_project.master.response;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HistorySmsResponse extends IdResponse {

    private String createdBy;

    private String createdDate;

    private String sendType;

    /**
     * Ô search tìm kiếm mục đích để tìm xem bảng nào là bảng sms_send_custom,sms_send
     */
    private String typeSend;

    private int totalSms;

    private int successNumber;

    private int failureNumber;

    private int receiversNumber;
}
