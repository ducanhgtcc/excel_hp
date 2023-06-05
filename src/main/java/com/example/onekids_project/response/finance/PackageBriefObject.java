package com.example.onekids_project.response.finance;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PackageBriefObject extends IdResponse {
    private String name;

    private boolean check;
}
