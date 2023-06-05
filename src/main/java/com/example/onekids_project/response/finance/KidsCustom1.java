package com.example.onekids_project.response.finance;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class KidsCustom1 extends IdResponse {

    private String fullName;

    private LocalDate birthDay;

    //số khoản ngoài khoản trong danh sách lớp
    private int number;

    private List<KidsPackageCustom1> fnKidsPackageList;

}

