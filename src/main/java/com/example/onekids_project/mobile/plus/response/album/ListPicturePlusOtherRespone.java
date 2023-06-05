package com.example.onekids_project.mobile.plus.response.album;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ListPicturePlusOtherRespone extends IdResponse {

    private String urlPicture;

    private Boolean isApproved;
}
