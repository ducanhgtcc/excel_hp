package com.example.onekids_project.request.album;

import com.example.onekids_project.request.base.BaseRequest;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchAlbumRequest extends BaseRequest {

    private String timeAlbumSearch;

    private Long idGrade;

    private Long idClass;

    private String albumType;

    @Override
    public String toString() {
        return "SearchAlbumRequest{" +
                "timeAlbumSearch='" + timeAlbumSearch + '\'' +
                ", idGrade=" + idGrade +
                ", idClass=" + idClass +
                ", albumType='" + albumType + '\'' +
                "} " + super.toString();
    }
}
