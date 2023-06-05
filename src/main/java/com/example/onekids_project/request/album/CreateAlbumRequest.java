package com.example.onekids_project.request.album;

import com.example.onekids_project.entity.classes.ListPicture;
import com.example.onekids_project.entity.kids.ExAlbumKids;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class CreateAlbumRequest {

    @NotBlank
    private String albumName;

    private String albumDescription;

    private String albumType;

    private Boolean albumApprovalDefault;

    private String urlPictureFirst;

    private Integer pictureNumber;

    private Integer pictureApprovedNumber;

    @NotNull
    private Long idClass;

    private List<ListPicture> listPicture;

    @NotNull
    private MultipartFile[] fileList;

//    private List<ExAlbumKids> exAlbumKidsList;
}
