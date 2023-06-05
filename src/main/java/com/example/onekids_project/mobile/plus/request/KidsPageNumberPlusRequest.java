package com.example.onekids_project.mobile.plus.request;

import com.example.onekids_project.mobile.request.PageNumberRequest;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class KidsPageNumberPlusRequest extends PageNumberRequest {
    @NotNull
    private Long idKid;

//    @NotNull
//    private Long idClass;
}
