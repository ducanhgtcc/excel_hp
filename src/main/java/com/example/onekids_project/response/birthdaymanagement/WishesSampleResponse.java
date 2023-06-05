package com.example.onekids_project.response.birthdaymanagement;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WishesSampleResponse extends IdResponse {

    private String wishesContent;

    private String receiverType;

    private String urlPicture;

    private String urlPictureLocal;

    private Long idSchool;
}
