package com.example.onekids_project.request.brand;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchSchoolBrandRequest {

    private String name;

    private Long  idAgent;

    @Override
    public String toString() {
        return "SearchSchoolBrandRequest{" +
                "name='" + name + '\'' +
                ", idAgent=" + idAgent +
                '}';
    }
}
