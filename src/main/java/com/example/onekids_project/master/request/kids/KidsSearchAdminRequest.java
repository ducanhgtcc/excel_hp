package com.example.onekids_project.master.request.kids;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.request.base.PageNumberWebRequest;
import com.example.onekids_project.validate.anotationcustom.StringInList;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class KidsSearchAdminRequest extends PageNumberWebRequest {
    @NotNull
    private Long idAgent;

    private Long idSchool;

    private boolean deleteStatus;

    @NotBlank
    @StringInList(values = {AppConstant.STUDYING, AppConstant.STUDY_WAIT, AppConstant.RESERVE, AppConstant.LEAVE_SCHOOL})
    private String status;

    private String nameOrPhone;

    @Override
    public String toString() {
        return "KidsSearchAdminRequest{" +
                "idAgent=" + idAgent +
                ", idSchool=" + idSchool +
                ", deleteStatus=" + deleteStatus +
                ", status='" + status + '\'' +
                ", nameOrPhone='" + nameOrPhone + '\'' +
                "} " + super.toString();
    }
}
