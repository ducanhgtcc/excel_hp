package com.example.onekids_project.response.finance.order;

import com.example.onekids_project.entity.finance.fees.OrderKidsHistory;
import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * date 2021-03-04 10:58
 *
 * @author lavanviet
 */
@Getter
@Setter
public class KidsPackagePaymentDetailResponse extends IdResponse {
    private double money;

    private OrderKidsHistoryCustom1 orderKidsHistory;
}

@Getter
@Setter
class OrderKidsHistoryCustom1 {
    private String name;

    private LocalDate date;

    private String description;
}


