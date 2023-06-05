package com.example.onekids_project.mobile.teacher.request.album;

import com.example.onekids_project.entity.classes.ListPicture;
import com.example.onekids_project.request.base.IdRequest;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

@Getter
@Setter
public class UpdateAlbumTeacherRequest extends IdRequest {

    private String albumName;

    private String albumDescription;

    private List<Long> idPictureList;

    private MultipartFile[] fileList;

    @Override
    public String toString() {
        return "UpdateAlbumTeacherRequest{" +
                "albumName='" + albumName + '\'' +
                ", albumDescription='" + albumDescription + '\'' +
                ", idPictureList=" + idPictureList +
                ", fileList=" + Arrays.toString(fileList) +
                "} " + super.toString();
    }
}
