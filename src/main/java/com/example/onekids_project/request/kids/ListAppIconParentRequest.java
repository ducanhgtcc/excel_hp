package com.example.onekids_project.request.kids;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ListAppIconParentRequest {
    private List<AppIconParentRequest> parentIcon1;

    private List<AppIconParentRequest> parentIcon2;
}
