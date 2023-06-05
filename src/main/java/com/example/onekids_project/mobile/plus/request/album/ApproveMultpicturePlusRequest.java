package com.example.onekids_project.mobile.plus.request.album;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
public class ApproveMultpicturePlusRequest {
    @NotNull
    private List<Long> idPictureList;

    @Override
    public String toString() {
        return "ApproveMultpicturePlusRequest{" +
                "idPictureList=" + idPictureList +
                '}';
    }
}
