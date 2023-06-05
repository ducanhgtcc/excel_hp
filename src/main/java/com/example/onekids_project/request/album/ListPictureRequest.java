package com.example.onekids_project.request.album;

import com.example.onekids_project.request.base.BaseRequest;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class ListPictureRequest {
    private String urlPicture;
    private MultipartFile multipartFilePicture;
    private Long idAlbum;
    private Boolean approved;
    private String  createdBy;
    private String createdDate;
    private String lastModifieBy;
    private String lastModifiteDate;
}
