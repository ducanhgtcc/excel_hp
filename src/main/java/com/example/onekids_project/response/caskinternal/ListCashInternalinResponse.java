package com.example.onekids_project.response.caskinternal;

import com.example.onekids_project.response.base.TotalResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ListCashInternalinResponse extends TotalResponse {

    private double totalMoney;

    List<CashInternalinResponse> responseList;
}
