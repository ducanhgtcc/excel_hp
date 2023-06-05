package com.example.onekids_project.mobile.parent.response.album;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AlbumMobileResponse extends IdResponse {

    private String albumName;

    private int pictureNumber;

    private String urlPictureFirst;

    private boolean albumNew;

    private LocalDateTime createdDate;
}
