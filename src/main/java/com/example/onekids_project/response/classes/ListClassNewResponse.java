package com.example.onekids_project.response.classes;

import com.example.onekids_project.response.base.TotalResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ListClassNewResponse extends TotalResponse {
    private List<ClassNewResponse> responseList;
}
