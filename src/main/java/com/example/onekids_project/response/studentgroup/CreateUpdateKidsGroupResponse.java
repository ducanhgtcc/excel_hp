package com.example.onekids_project.response.studentgroup;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateUpdateKidsGroupResponse extends IdResponse {
    private String groupName;

    private String description;
}
