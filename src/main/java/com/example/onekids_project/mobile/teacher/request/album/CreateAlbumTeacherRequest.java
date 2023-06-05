package com.example.onekids_project.mobile.teacher.request.album;

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
public class CreateAlbumTeacherRequest {

    @NotBlank
    private String albumName;

    private String albumDescription;

    private String albumType;

//    private Long idClass;

    private List<ListPicture> listPicture;

    @NotNull
    private MultipartFile[] fileList;

    private List<ExAlbumKids> exAlbumKidsList;
}
