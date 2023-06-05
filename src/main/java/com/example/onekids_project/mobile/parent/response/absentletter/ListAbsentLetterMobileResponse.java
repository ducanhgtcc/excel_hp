package com.example.onekids_project.mobile.parent.response.absentletter;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ListAbsentLetterMobileResponse {

    private boolean lastPage;

    private List<AbsentLetterMobileResponse> absentLetterMobileResponseList;
}
