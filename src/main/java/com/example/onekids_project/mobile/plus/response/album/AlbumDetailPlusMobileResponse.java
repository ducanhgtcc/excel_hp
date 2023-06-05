package com.example.onekids_project.mobile.plus.response.album;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AlbumDetailPlusMobileResponse extends IdResponse {

    private String albumType;

    private String albumName;

    private String albumDescription;

    private String createdBy;

    private String createdDate;

    private Integer pictureNumber;

    private boolean approved;

    private String urlPictureFirst;

    private Long idClass;

    private List<ListPicturePlusOtherRespone> listPictureList;
}
