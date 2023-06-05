package com.example.onekids_project.mobile.parent.response.finance.order;

import lombok.Getter;
import lombok.Setter;

/**
 * date 2021-03-16 16:34
 *
 * @author lavanviet
 */
@Getter
@Setter
public class OrderKidsStatisticalResponse {
    private long moneyInTotal;

    private long moneyPaidInTotal;

    private long moneyRemainInTotal;

    private long moneyOutTotal;

    private long moneyPaidOutTotal;

    private long moneyRemainOutTotal;
}
