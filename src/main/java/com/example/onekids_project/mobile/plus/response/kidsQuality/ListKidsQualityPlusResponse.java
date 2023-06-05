package com.example.onekids_project.mobile.plus.response.kidsQuality;

import com.example.onekids_project.mobile.response.LastPageBase;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ListKidsQualityPlusResponse extends LastPageBase {

    private List<KidsQualityPlusResponse> dataList;

}
