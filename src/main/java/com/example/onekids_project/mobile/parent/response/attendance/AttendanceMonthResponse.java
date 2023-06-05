package com.example.onekids_project.mobile.parent.response.attendance;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AttendanceMonthResponse {
    private int date;

    //true->co phep, false->khong phep
    private boolean status;
}
