package com.example.onekids_project.request.cashinternal;

import com.example.onekids_project.common.FinanceConstant;
import com.example.onekids_project.request.base.IdRequest;
import com.example.onekids_project.validate.anotationcustom.StringInList;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class UpdateBankInfoRequest extends IdRequest {

    @NotNull
    private String fullName;

    @NotNull
    private String accountNumber;

    @NotNull
    private String bankName;

    private String branch;

    private String description;

    @Override
    public String toString() {
        return "UpdatebankInfoRequest{" +
                "fullName='" + fullName + '\'' +
                ", accountNumber='" + accountNumber + '\'' +
                ", bankName='" + bankName + '\'' +
                ", branch='" + branch + '\'' +
                ", description='" + description + '\'' +
                "} " + super.toString();
    }
}
