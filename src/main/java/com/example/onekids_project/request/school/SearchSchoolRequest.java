package com.example.onekids_project.request.school;

import com.example.onekids_project.request.base.BaseRequest;
import com.example.onekids_project.request.base.PageNumberWebRequest;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class SearchSchoolRequest extends PageNumberWebRequest {

    @NotNull
    private Long idAgent;

    private Long idSchool;

    private boolean deleteStatus;

    private Boolean activated;

    private String name;

    @Override
    public String toString() {
        return "SearchSchoolRequest{" +
                "idAgent=" + idAgent +
                ", idSchool=" + idSchool +
                ", deleteStatus=" + deleteStatus +
                ", activated=" + activated +
                ", name='" + name + '\'' +
                "} " + super.toString();
    }
}
