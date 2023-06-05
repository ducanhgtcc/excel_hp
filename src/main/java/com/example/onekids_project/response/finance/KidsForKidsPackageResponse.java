package com.example.onekids_project.response.finance;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class KidsForKidsPackageResponse extends IdResponse {

    private String fullName;

    private LocalDate birthDay;

    private List<KidsPackageCustom2> fnKidsPackageList;
}



