package com.example.onekids_project.response.brandManagement;

import com.example.onekids_project.response.base.TotalResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ListFindAgentBrandResponse extends TotalResponse {
    private List<FindAgentBrandResponse> dataList;
}
