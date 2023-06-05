package com.example.onekids_project.mobile.plus.request.album;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
public class DeleteMultpicturePlusRequest {
    @NotNull
    private List<Long> idPictureList;

    @Override
    public String toString() {
        return "DeleteMultpicturePlusRequest{" +
                "idPictureList=" + idPictureList +
                '}';
    }
}
