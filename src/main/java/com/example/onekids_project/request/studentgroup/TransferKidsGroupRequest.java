package com.example.onekids_project.request.studentgroup;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;

@Setter
@Getter
public class TransferKidsGroupRequest {
    @NotNull
    private Long id;

    private List<Long> idKidsGroupList;
}
