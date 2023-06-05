package com.example.onekids_project.request.kids;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.request.base.PageNumberWebRequest;
import com.example.onekids_project.validate.anotationcustom.StringInList;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class SearchKidsRequest extends PageNumberWebRequest {

    private String loginStatus;

    @NotBlank
    @StringInList(values = {AppConstant.STUDYING, AppConstant.STUDY_WAIT, AppConstant.RESERVE, AppConstant.LEAVE_SCHOOL})
    private String status;

    private Long idGrade;

    private Long idClass;

    private String nameOrPhone;

    @NotBlank
    @StringInList(values = {AppConstant.KID_SEARCH_START_DATE, AppConstant.KID_SEARCH_BIRTHDAY})
    private String type;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private List<LocalDate> dateList;

    @Override
    public String toString() {
        return "SearchKidsRequest{" +
                "loginStatus='" + loginStatus + '\'' +
                ", status='" + status + '\'' +
                ", idGrade=" + idGrade +
                ", idClass=" + idClass +
                ", nameOrPhone='" + nameOrPhone + '\'' +
                ", type='" + type + '\'' +
                ", dateList=" + dateList +
                "} " + super.toString();
    }
}
