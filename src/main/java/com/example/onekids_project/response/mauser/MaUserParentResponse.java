package com.example.onekids_project.response.mauser;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MaUserParentResponse extends IdResponse {

    private String fullName;

    private String phone;

    private List<KidsInforMauserResponse> kidList;

}
