package com.example.onekids_project.integration.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SmsResultDTO {
    private String phone;
    //mã nhà mạng trả về: bao gồm mã lỗi và id, ngăn cách bằng dấu |
    //bằng 1. thành công
    //khác 1. lỗi
    private String errCode;
    //nội dung nhà mạng cho
    private String errMsg;
    //trạng thái thành công: true
    private boolean success;
    //mã lỗi lấy tách từ errCode
    private long errCodeId;
}
