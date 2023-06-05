package com.example.onekids_project.mobile.plus.request.album;

import com.example.onekids_project.mobile.request.PageNumberRequest;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
public class SearchAlbumPlusRequest extends PageNumberRequest {


    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate date;

    private Long idClass;

    @Override
    public String toString() {
        return "SearchAlbumPlusRequest{" +
                "date=" + date +
                ", idClass=" + idClass +
                "} " + super.toString();
    }
}
