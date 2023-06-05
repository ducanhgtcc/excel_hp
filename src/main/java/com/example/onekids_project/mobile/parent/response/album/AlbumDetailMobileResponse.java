package com.example.onekids_project.mobile.parent.response.album;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class AlbumDetailMobileResponse {

    private String albumType;

    private String albumName;

    private String albumDescription;

    private String createdBy;

    private LocalDateTime createdDate;

    private Integer pictureNumber;

    private String urlPictureFirst;

    private List<String> pictureList;
}
