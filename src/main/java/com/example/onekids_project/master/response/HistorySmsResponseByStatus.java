package com.example.onekids_project.master.response;

import lombok.Getter;
import lombok.Setter;

/**
 * Tìm kiếm lịch sử sms theo trạng thái thành công hay thất bại
 */
@Getter
@Setter
public class HistorySmsResponseByStatus {
    private String fullName;

    private String phone;

    private String appType;

    private String className;

    private String content;

    private String status;
}
