package com.example.onekids_project.response.studentgroup;

import com.example.onekids_project.response.base.TotalResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ListKidsGroupResponse extends TotalResponse {
    List<KidsGroupResponse> kidsGroupResponseList;
}
