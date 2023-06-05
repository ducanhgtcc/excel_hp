package com.example.onekids_project.mobile.plus.response.album;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
public class DeleteMultialbumPlusRequest {
    @NotNull
    private List<Long> idAlbumList;

    @Override
    public String toString() {
        return "DeleteMultialbumPlusRequest{" +
                "idAlbumList=" + idAlbumList +
                '}';
    }
}
