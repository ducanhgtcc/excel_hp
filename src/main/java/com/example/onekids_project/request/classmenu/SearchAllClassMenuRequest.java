package com.example.onekids_project.request.classmenu;

import com.example.onekids_project.request.base.BaseRequest;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchAllClassMenuRequest extends BaseRequest {

    private String timeClassMenu;
    private Long idClass;
    private Long idGrade;
    private String isMonday;

    @Override
    public String toString() {
        return "SearchAllClassMenuRequest{" +
                "timeClassMenu='" + timeClassMenu + '\'' +
                ", idClass=" + idClass +
                ", idGrade=" + idGrade +
                ", isMonday='" + isMonday + '\'' +
                "} " + super.toString();
    }
}
