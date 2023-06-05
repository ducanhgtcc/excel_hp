package com.example.onekids_project.mobile.plus.response.album;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class DeleteMultpicturePlusRequest {
    @NotNull
    private List<Long> idPictureList;
}
