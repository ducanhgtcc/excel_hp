package com.example.onekids_project.response.album;

import com.example.onekids_project.entity.classes.Album;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ListPictureResponse {
    private Long id;

    private String urlPicture;

    private Boolean isApproved;

    @JsonBackReference
    private Album album;
}
