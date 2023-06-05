package com.example.onekids_project.response.kids;

import com.example.onekids_project.response.base.IdResponse;
import com.example.onekids_project.response.classes.MaClassOtherResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KidsInGroupResponse extends IdResponse {
    private String fullName;

    private String kidStatus;

    private MaClassOtherResponse maClass;

}
