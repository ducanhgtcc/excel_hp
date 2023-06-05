package com.example.onekids_project.request.kidsheightweight;

import com.example.onekids_project.common.KidsStatusConstant;
import com.example.onekids_project.request.base.BaseRequest;
import com.example.onekids_project.validate.anotationcustom.StringInList;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class SearchKidsHeightWeightRequest extends BaseRequest {

//    @NotBlank
    private String date;

    @NotBlank
    @StringInList(values = {KidsStatusConstant.STUDYING, KidsStatusConstant.STUDY_WAIT, KidsStatusConstant.RESERVE, KidsStatusConstant.LEAVE_SCHOOL})
    private String status;

//    @NotNull
    private Long idClass;

    private String codeOrName;

    private List<Long> idKidsList;

    @Override
    public String toString() {
        return "SearchKidsHeightWeightRequest{" +
                "date='" + date + '\'' +
                ", status='" + status + '\'' +
                ", idClass=" + idClass +
                ", codeOrName='" + codeOrName + '\'' +
                ", idKidsList=" + idKidsList +
                "} " + super.toString();
    }
}
