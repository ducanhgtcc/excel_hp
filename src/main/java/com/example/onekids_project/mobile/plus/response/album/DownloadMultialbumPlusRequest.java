package com.example.onekids_project.mobile.plus.response.album;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
public class DownloadMultialbumPlusRequest {
    @NotNull
    private List<Long> idAlbumList;


}
