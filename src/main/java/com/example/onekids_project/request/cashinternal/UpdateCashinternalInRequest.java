package com.example.onekids_project.request.cashinternal;

import com.example.onekids_project.common.FinanceConstant;
import com.example.onekids_project.request.base.IdRequest;
import com.example.onekids_project.validate.anotationcustom.StringInList;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
public class UpdateCashinternalInRequest extends IdRequest {

    @NotNull
    private Double money;

    @NotNull
    private LocalDate date;

    @NotNull
    private Long idPeopleTypeInternal;

    @NotNull
    private Long idPeopleTypeOther;

    private boolean payment;

    private String content;

    @Override
    public String toString() {
        return "UpdateCashinternalInRequest{" +
                ", date=" + date +
                ", idPeopleTypeInternal=" + idPeopleTypeInternal +
                ", idPeopleTypeOther=" + idPeopleTypeOther +
                ", payment=" + payment +
                ", content='" + content + '\'' +
                "} " + super.toString();
    }
}
