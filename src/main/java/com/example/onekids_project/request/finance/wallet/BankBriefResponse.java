package com.example.onekids_project.request.finance.wallet;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;

/**
 * date 2021-02-25 13:33
 * 
 * @author lavanviet
 */
@Getter
@Setter
public class BankBriefResponse extends IdResponse {
    private String  fullName;

    private String accountNumber;

    private String bankName;
}
