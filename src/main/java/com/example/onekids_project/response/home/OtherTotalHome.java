package com.example.onekids_project.response.home;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OtherTotalHome {
    //lời nhắn
    private int message;

    //dặn thuốc
    private int medicine;

    //số hs nghỉ học
    private int absent;

    //số đơn xin nghỉ chưa duyệt tất cả các ngày
    private int absentLetterNoConfirm;
}
