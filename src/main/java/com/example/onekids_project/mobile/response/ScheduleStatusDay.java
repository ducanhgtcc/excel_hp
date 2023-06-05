package com.example.onekids_project.mobile.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ScheduleStatusDay {

    // T2,T3,T4..
    String keyDay;

    // yes-> có tkb, no->ko có tkb, absent-> nghỉ học
    String status;
}
