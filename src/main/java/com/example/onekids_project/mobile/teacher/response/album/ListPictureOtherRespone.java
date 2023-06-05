package com.example.onekids_project.mobile.teacher.response.album;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ListPictureOtherRespone extends IdResponse {

    private String urlPicture;

    private Boolean isApproved;
}
