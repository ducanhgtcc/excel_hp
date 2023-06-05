package com.example.onekids_project.mobile.plus.response.album;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AlbumPlusMobileResponse extends IdResponse {

    private String albumName;

    private int pictureNumber;

    private String urlPictureFirst;

    private boolean approved;

    private boolean albumNew;

    private String createdDate;
}
