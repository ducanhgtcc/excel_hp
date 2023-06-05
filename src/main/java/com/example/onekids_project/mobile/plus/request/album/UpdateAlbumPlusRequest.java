package com.example.onekids_project.mobile.plus.request.album;

import com.example.onekids_project.request.base.IdRequest;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

@Getter
@Setter
public class UpdateAlbumPlusRequest extends IdRequest {

    private String albumName;

    private String albumDescription;

    private List<Long> idPictureList;

    private MultipartFile[] fileList;

    @NotNull
    private Long idClass;

    @Override
    public String toString() {
        return "UpdateAlbumPlusRequest{" +
                "albumName='" + albumName + '\'' +
                ", albumDescription='" + albumDescription + '\'' +
                ", idPictureList=" + idPictureList +
                ", fileList=" + Arrays.toString(fileList) +
                ", idClass=" + idClass +
                "} " + super.toString();
    }
}
