package com.example.onekids_project.request.studentgroup;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class CreateKidsGroupRequest {
    @NotBlank
    private String groupName;

    private String description;
}
