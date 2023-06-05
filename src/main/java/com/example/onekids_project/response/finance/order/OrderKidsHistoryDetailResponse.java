package com.example.onekids_project.response.finance.order;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

/**
 * date 2021-03-03 14:33
 *
 * @author lavanviet
 */
@Getter
@Setter
public class OrderKidsHistoryDetailResponse extends IdResponse {
    private double money;

    //ten khoản thu
    private String name;
}
