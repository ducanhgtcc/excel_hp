package com.example.onekids_project.request.kids;

import com.example.onekids_project.request.base.IdRequest;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class ActivedOneKidsRequest extends IdRequest {
    @NotNull
    private Boolean checkOneActive;
}
