package com.example.onekids_project.response.studentgroup;

import com.example.onekids_project.dto.KidsDTO;
import com.example.onekids_project.response.base.IdResponse;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class KidsGroupResponse extends IdResponse {

    private String groupName;

    private String description;

    private int kidsNumber;

    private List<Long> idKidsList;


    private List<KidsDTO> kidsList;
}
