package com.example.onekids_project.request.smsNotify;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
public class SmsSearchKidsRequest {
    List<Long> idClassList;

    List<Long> idGradeList;

}
