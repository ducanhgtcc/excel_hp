package com.example.onekids_project.response.schoolconfig;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClassConfigResponse extends IdResponse {
    private String className;

    private boolean morningSaturday;

    private boolean afternoonSaturday;

    private boolean eveningSaturday;

    private boolean sunday;
}
