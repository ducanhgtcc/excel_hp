package com.example.onekids_project.mobile.parent.response.album;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ListAlbumMobileResponse {

    private boolean lastPage;

    private List<AlbumMobileResponse> listAllAlbum;
}
