package com.example.onekids_project.response.birthdaymanagement;

import com.example.onekids_project.dto.ParentDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ListParentResponse {
    List<ParentDTO> parentlist;
}
