package com.example.onekids_project.mobile.teacher.request.album;

import com.example.onekids_project.request.base.IdRequest;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class DeleteMultialbumTeacherRequest {
    @NotNull
    private List<Long> idAlbumList;
}
