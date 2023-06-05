package com.example.onekids_project.response.classes;

import com.example.onekids_project.dto.MaClassDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ListMaClassResponse {
    List<MaClassDTO> maClassList;
}
