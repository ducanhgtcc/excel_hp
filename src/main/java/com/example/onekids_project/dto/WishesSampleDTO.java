package com.example.onekids_project.dto;

import com.example.onekids_project.dto.base.IdDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WishesSampleDTO extends IdDTO {

    private String wishesContent;

    private String receiverType;

    private String urlPicture;

    private String urlPictureLocal;

    private Long idSchool;
}
