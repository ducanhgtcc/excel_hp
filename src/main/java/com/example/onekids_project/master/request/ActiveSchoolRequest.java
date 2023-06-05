package com.example.onekids_project.master.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ActiveSchoolRequest {
    private List<Long> ids;

    private boolean activeOrUnActive;
}
