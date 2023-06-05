package com.example.onekids_project.response.cyclemoney;

import com.example.onekids_project.request.cyclemoney.CycleMoneyRequest;
import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

/**
 * @author lavanviet
 */
@Getter
@Setter
public class CycleMoneyResponse extends CycleMoneyRequest {
    private Long id;
}
