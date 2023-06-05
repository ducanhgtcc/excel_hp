package com.example.onekids_project.mobile.plus.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetLinkPlusRequest {

    private Long idClass;


    private String dateInput;

    @Override
    public String toString() {
        return "GetLinkPlusRequest{" +
                "idClass=" + idClass +
                ", dateInput='" + dateInput + '\'' +
                '}';
    }
}
