package com.example.onekids_project.mobile.plus.response.KidsReco;

import com.example.onekids_project.mobile.response.ListFileNotifi;
import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DetailClassResponse extends IdResponse {

    private String fullName;

    private String avatar;

    private String birthday;

    private boolean status;
}
