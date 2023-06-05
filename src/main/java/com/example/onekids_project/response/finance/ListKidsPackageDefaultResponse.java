package com.example.onekids_project.response.finance;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ListKidsPackageDefaultResponse {
    private List<PackageBriefObject> packageList;

    private List<KidsPackageDefaultResponse> dataList;
}
