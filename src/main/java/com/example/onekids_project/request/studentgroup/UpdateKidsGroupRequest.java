package com.example.onekids_project.request.studentgroup;

import com.example.onekids_project.request.base.IdRequest;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class UpdateKidsGroupRequest extends IdRequest {
    @NotBlank
    private String groupName;

    private String description;
}
