package com.example.onekids_project.mobile.parent.response.home;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class KidsInforResponse {
    private String avatar;

    private String address;

    private String representation;

    private List<ParentInforObject> parentList;
}
