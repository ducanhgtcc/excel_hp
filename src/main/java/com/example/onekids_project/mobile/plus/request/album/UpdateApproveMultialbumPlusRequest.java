package com.example.onekids_project.mobile.plus.request.album;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
public class UpdateApproveMultialbumPlusRequest {

    @NotNull
    private List<Long> idAlbumList;

    @Override
    public String toString() {
        return "UpdateApproveMultialbumPlusRequest{" +
                "idAlbumList=" + idAlbumList +
                '}';
    }
}
