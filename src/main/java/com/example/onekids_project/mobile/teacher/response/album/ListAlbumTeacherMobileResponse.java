package com.example.onekids_project.mobile.teacher.response.album;

import com.example.onekids_project.mobile.response.LastPageBase;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ListAlbumTeacherMobileResponse extends LastPageBase {

    private List<AlbumTeacherMobileResponse> dataList;
}
