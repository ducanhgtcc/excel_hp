package com.example.onekids_project.mobile.parent.response.finance.order;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * date 2021-03-16 13:24
 *
 * @author lavanviet
 */
@Getter
@Setter
public class OrderKidsParentResponse extends IdResponse {
    private int month;

    private boolean parentRead;

    private long moneyTotal;

    private long moneyRemainTotal;

    private ListOrderKidsCustom dataIn;

    private ListOrderKidsCustom dataOut;
}
