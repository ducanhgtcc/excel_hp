package com.example.onekids_project.response.finance;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ListKidsPackageInClassResponse {
    private List<PackageBriefObject> packageList;

    private List<KidsCustom1> dataList;

}

