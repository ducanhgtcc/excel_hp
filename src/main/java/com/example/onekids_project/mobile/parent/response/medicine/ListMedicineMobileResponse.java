package com.example.onekids_project.mobile.parent.response.medicine;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ListMedicineMobileResponse {

    private boolean lastPage;

    private List<MedicineMobileResponse> medicineMobileResponseList;
}
