package com.example.onekids_project.response.wishes;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;

@Getter
@Setter
public class WishesSampleResponse extends IdResponse {
    private String wishesContent;

    private String receiverType;

    private String urlPicture;

    private Long idSchool;

}
