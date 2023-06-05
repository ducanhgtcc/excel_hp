package com.example.onekids_project.mobile.teacher.response.album;

import com.example.onekids_project.entity.classes.ListPicture;
import com.example.onekids_project.response.base.IdResponse;
import com.example.onekids_project.response.kids.KidOtherResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class AlbumDetailTeacherMobileResponse extends IdResponse {

    private String albumType;

    private String albumName;

    private String albumDescription;

    private String createdBy;

    private String createdDate;

    private Integer pictureNumber;

    private boolean confirmStatus;

    private String urlPictureFirst;

    private List<ListPictureOtherRespone> listPictureList;
}
