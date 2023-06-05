package com.example.onekids_project.response.caskinternal;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PeopleTypeOtherResponse extends IdResponse {

    private String name;

    private String type;

    private boolean defaultStatus;
}
