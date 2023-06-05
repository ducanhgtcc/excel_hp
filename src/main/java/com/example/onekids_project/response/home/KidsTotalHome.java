package com.example.onekids_project.response.home;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KidsTotalHome {
    //tổng số học sinh đi học+bảo lưu
    private int total;

    //đang học
    private int study;

    //bảo lưu
    private int reserve;

    //chờ học
    private int wait;
}
