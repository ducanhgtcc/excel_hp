package com.example.onekids_project.request.kids;

import com.example.onekids_project.request.base.IdRequest;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class MergeKidsRequest extends IdRequest {
    @NotNull
    private Long idUser;

}
