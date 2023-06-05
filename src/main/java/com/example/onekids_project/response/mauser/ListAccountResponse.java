package com.example.onekids_project.response.mauser;

import com.example.onekids_project.response.base.TotalResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ListAccountResponse extends TotalResponse {
    private List<AccountResponse> dataList;
}
