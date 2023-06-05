package com.example.onekids_project.response.kids;

import com.example.onekids_project.dto.KidsDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ListKidsResponse {
    List<KidsDTO> kidList;
}
