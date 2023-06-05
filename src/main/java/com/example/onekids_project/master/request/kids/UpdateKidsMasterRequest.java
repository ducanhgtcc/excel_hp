package com.example.onekids_project.master.request.kids;

import com.example.onekids_project.request.kids.AppIconParentRequest;
import com.example.onekids_project.request.kids.KidsExtraInfoRequest;
import com.example.onekids_project.request.kids.UpdateKidMainInforRequest;
import com.example.onekids_project.request.kids.UpdateParentInforRequest;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import java.util.List;

@Getter
@Setter
public class UpdateKidsMasterRequest {

    private Long idSchool;

    @Valid
    UpdateKidMainInforRequest kidMainInfo;

    @Valid
    UpdateParentInforRequest parentInfo;

    KidsExtraInfoRequest kidsExtraInfo;

    List<AppIconParentRequest> parentIconApp;
}
