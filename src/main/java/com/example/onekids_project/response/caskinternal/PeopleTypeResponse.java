package com.example.onekids_project.response.caskinternal;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PeopleTypeResponse extends IdResponse {

    private String name;

    private String phone;

    private String birthday;

    private String gender;

    private String email;

    private String address;

    private String indentify;

    private String description;

    private String type;

    private String code;

    private boolean defaultStatus;
}
