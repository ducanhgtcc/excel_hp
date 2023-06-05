package com.example.onekids_project.master.request;

import com.example.onekids_project.request.base.PageNumberWebRequest;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class SchoolMasterSearchRequest extends PageNumberWebRequest {
    @NotNull
    private Long idAgent;

    private Long idSchool;

    private boolean deleteStatus;

    private Boolean activated;

    private String nameOrPhone;

    @Override
    public String toString() {
        return "SchoolMasterSearchRequest{" +
                "idSchool=" + idSchool +
                ", deleteStatus=" + deleteStatus +
                ", actived=" + activated +
                ", nameOrPhone='" + nameOrPhone + '\'' +
                "} " + super.toString();
    }
}
