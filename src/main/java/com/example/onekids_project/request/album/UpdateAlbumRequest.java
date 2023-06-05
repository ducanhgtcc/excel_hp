package com.example.onekids_project.request.album;

import com.example.onekids_project.entity.classes.ListPicture;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class UpdateAlbumRequest {
    private Long id;

    private String albumName;

    private String albumDescription;

    private String albumType;

    private String schoolApprovalDate;

    private Long idClass;

    private List<ListPicture> listPicture;

    private MultipartFile[] fileList;

}
