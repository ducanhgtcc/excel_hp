package com.example.onekids_project.request.kids.transfer;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.request.base.PageNumberWebRequest;
import com.example.onekids_project.validate.anotationcustom.StringInList;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.List;
@Getter
@Setter
public class SearchKidsTransferRequest extends PageNumberWebRequest {

    @NotBlank
    @StringInList(values = {AppConstant.STUDYING, AppConstant.STUDY_WAIT, AppConstant.RESERVE, AppConstant.LEAVE_SCHOOL})
    private String status;

    private Long idGrade;

    private Long idClass;

    private String nameOrPhone;

    @Override
    public String toString() {
        return "SearchKidsTransferRequest{" +
                "status='" + status + '\'' +
                ", idGrade=" + idGrade +
                ", idClass=" + idClass +
                ", nameOrPhone='" + nameOrPhone + '\'' +
                "} " + super.toString();
    }
}
