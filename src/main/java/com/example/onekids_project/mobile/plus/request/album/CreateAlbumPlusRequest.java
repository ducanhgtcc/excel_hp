package com.example.onekids_project.mobile.plus.request.album;

import com.example.onekids_project.entity.classes.ListPicture;
import com.example.onekids_project.entity.kids.ExAlbumKids;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

@Getter
@Setter
public class CreateAlbumPlusRequest {

    @NotNull
    private String albumName;

    private String albumDescription;

    private String albumType;

    private Long idClass;

    @NotNull
    private MultipartFile[] fileList;

    @Override
    public String toString() {
        return "CreateAlbumPlusRequest{" +
                "albumName='" + albumName + '\'' +
                ", albumDescription='" + albumDescription + '\'' +
                ", albumType='" + albumType + '\'' +
                ", idClass=" + idClass +
                ", fileList=" + Arrays.toString(fileList) +
                '}';
    }
}
