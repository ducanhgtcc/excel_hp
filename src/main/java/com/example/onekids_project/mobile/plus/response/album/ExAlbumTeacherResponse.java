package com.example.onekids_project.mobile.plus.response.album;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ExAlbumTeacherResponse extends IdResponse {

    private String albumName;

    private int pictureNumber;

    private String urlPictureFirst;

    private boolean isApproved;

    private boolean albumNew;

    private LocalDateTime createdDate;
}
