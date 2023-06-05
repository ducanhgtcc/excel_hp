package com.example.onekids_project.request.album;

import com.example.onekids_project.request.base.PageNumberWebRequest;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
public class SearchAlbumNewRequest extends PageNumberWebRequest {

    private String timeAlbumSearch;

    private Long idGrade;

    private Long idClass;

    private String albumType;

    @Override
    public String toString() {
        return "SearchAlbumNewRequest{" +
                "timeAlbumSearch='" + timeAlbumSearch + '\'' +
                ", idGrade=" + idGrade +
                ", idClass=" + idClass +
                ", albumType='" + albumType + '\'' +
                "} " + super.toString();
    }
}
