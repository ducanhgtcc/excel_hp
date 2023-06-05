package com.example.onekids_project.response.finance.order;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * date 2021-02-23 15:29
 * 
 * @author lavanviet
 */
@Getter
@Setter
public class OrderKidsResponse extends IdResponse {
    private String fullName;

    private LocalDate birthDay;

    private OrderKidsCustom1 orderKids;

    //số hóa đơn các tháng trước chưa hoàn thành
    private int inCompleteOrderNumber;

}
