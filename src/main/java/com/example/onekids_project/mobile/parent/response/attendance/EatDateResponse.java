package com.example.onekids_project.mobile.parent.response.attendance;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class EatDateResponse {
    private LocalDate date;

    private List<String> dataList;
}
