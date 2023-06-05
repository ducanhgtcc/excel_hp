package com.example.onekids_project.mobile.plus.response.medicine;

import com.example.onekids_project.mobile.response.LastPageBase;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ListMedicinePlusResponse extends LastPageBase {

    private List<MedicinePlusResponse> dataList;

}
