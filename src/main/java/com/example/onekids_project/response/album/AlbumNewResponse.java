package com.example.onekids_project.response.album;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AlbumNewResponse {

    private Long id;

    private String albumName;

    private String albumDescription;

    private String albumType;

    private Boolean albumApprovalDefault;

    private Long idSchoolApproval;

    private LocalDateTime schoolApprovalDate;

    private String urlPictureFirst;

    private Integer pictureNumber;

    private Integer pictureApprovedNumber;

    private Boolean albumView;

    private Boolean albumNew;

    private Long idSchool;

    private LocalDateTime createdDate;

    private String createdBy;

    private Long idClass;

    private Boolean checkApprove=false;

}
