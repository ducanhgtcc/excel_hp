package com.example.onekids_project.mobile.parent.response.kids;

import com.example.onekids_project.mobile.response.LastPageBase;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ListKidsHeightWeightParentResponse extends LastPageBase {
    private List<KidsHeightWeightParentResponse> dataList;
}
