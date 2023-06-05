package com.example.onekids_project.request.album;

import com.example.onekids_project.entity.classes.ListPicture;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
public class UpdateAlbumApprovedRequest {
    private Long id;
}
