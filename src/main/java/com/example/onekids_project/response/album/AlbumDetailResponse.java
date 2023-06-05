package com.example.onekids_project.response.album;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class AlbumDetailResponse {

    private Long id;

    private String albumName;

    private String albumDescription;

    private Integer pictureNumber;

    private Integer pictureApprovedNumber;

    private String createdBy;

    private LocalDateTime createdDate;

    private String albumType;

    private Boolean albumApprovalDefault;

    private Boolean albumView;


    List<ListPictureResponse> alistPictureList;

    private Long idClass;
}
