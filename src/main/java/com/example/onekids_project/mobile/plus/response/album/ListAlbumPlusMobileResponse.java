package com.example.onekids_project.mobile.plus.response.album;

import com.example.onekids_project.mobile.response.LastPageBase;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ListAlbumPlusMobileResponse extends LastPageBase {

    private List<AlbumPlusMobileResponse> dataList;
}
